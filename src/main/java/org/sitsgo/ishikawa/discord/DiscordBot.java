package org.sitsgo.ishikawa.discord;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.*;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;
import org.reactivestreams.Publisher;
import org.sitsgo.ishikawa.announcement.game.GameAnnouncementEvent;
import org.sitsgo.ishikawa.announcement.rankup.RankUpAnnouncementEvent;
import org.sitsgo.ishikawa.discord.command.DiscordButtonCommand;
import org.sitsgo.ishikawa.discord.command.DiscordCommand;
import org.sitsgo.ishikawa.discord.command.DiscordMenuCommand;
import org.sitsgo.ishikawa.discord.command.DiscordModalCommand;
import org.sitsgo.ishikawa.go.Game;
import org.sitsgo.ishikawa.gowebsite.ffg.FFGWebsite;
import org.sitsgo.ishikawa.i18n.I18n;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiscordBot {
    private static final Logger log = LoggerFactory.getLogger(DiscordBot.class);

    private final DiscordBotConfiguration configuration;

    private final MemberRepository memberRepository;

    private final FFGWebsite ffgWebsite;

    private final I18n i18n;

    private GatewayDiscordClient client;

    @Autowired
    private List<DiscordCommand> commands;

    @Autowired
    private List<DiscordModalCommand> modalCommands;

    @Autowired
    private List<DiscordButtonCommand> buttonCommands;

    public DiscordBot(
            DiscordBotConfiguration configuration,
            MemberRepository memberRepository,
            FFGWebsite ffgWebsite,
            I18n i18n
    ) {
        this.configuration = configuration;
        this.memberRepository = memberRepository;
        this.ffgWebsite = ffgWebsite;
        this.i18n = i18n;
    }

    public void login() {
        client = DiscordClientBuilder.create(configuration.getToken())
                .build()
                .login()
                .block();

        if (client != null) {
            initRouter();
            initMenu();
            initModal();
            initButton();

            log.info("Discord Bot successfully logged in");
        } else {
            log.error("Discord Bot disabled");
        }
    }

    private boolean isDisabled() {
        return client == null;
    }

    private void initRouter() {
        client.on(ChatInputInteractionEvent.class, event -> {
            String requestedCommandName = event.getCommandName();
            Member member = getMemberForEvent(event);

            for (DiscordCommand command : commands) {
                if (command.getName().equals(requestedCommandName)) {
                    return command.run(event, member);
                }
            }

            return interactionNotFound(event);
        }).subscribe();
    }

    private void initMenu() {
        client.on(SelectMenuInteractionEvent.class, event -> {
            String requestedMenuId = event.getCustomId();
            Member member = getMemberForEvent(event);

            for (DiscordCommand command : commands) {
                if (command instanceof DiscordMenuCommand menuCommand) {
                    if (menuCommand.getMenuIds().contains(requestedMenuId)) {
                        return menuCommand.onMenuChange(event, member);
                    }
                }
            }

            return interactionNotFound(event);
        }).subscribe();
    }

    private void initModal() {
        client.on(ModalSubmitInteractionEvent.class, event -> {
            String requestedModalId = event.getCustomId();
            Member member = getMemberForEvent(event);

            for (DiscordModalCommand command : modalCommands) {
                if (command.getModalIds().contains(requestedModalId)) {
                    Publisher<Void> done = command.onModalSubmit(event, member);
                    memberRepository.save(member);

                    return done;
                }
            }

            return interactionNotFound(event);
        }).subscribe();
    }

    private void initButton() {
        client.on(ButtonInteractionEvent.class, event -> {
            String requestedButtonId = event.getCustomId();
            Member member = getMemberForEvent(event);

            for (DiscordButtonCommand command : buttonCommands) {
                if (command.getButtonIds().contains(requestedButtonId)) {
                    Publisher<Void> result = command.onButtonClick(event, member);
                    memberRepository.save(member);

                    return result;
                }
            }

            return interactionNotFound(event);
        }).subscribe();
    }

    private InteractionApplicationCommandCallbackReplyMono interactionNotFound(DeferrableInteractionEvent event) {
        return event.reply(i18n.t("discord.notfound"))
                .withEphemeral(true);
    }

    @EventListener
    public void onApplicationEvent(GameAnnouncementEvent event) {
        announceGame(event.game());
    }

    public void announceGame(Game game) {
        if (isDisabled()) {
            return;
        }

        EmbedCreateSpec embed = createGameAnnouncementEmbed(game);

        client.getChannelById(configuration.getGameAnnouncementChannelId())
                .ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.createMessage(embed))
                .subscribe();
    }

    private EmbedCreateSpec createGameAnnouncementEmbed(Game game) {
        EmbedCreateSpec.Builder embed = EmbedCreateSpec.builder()
                .color(Color.RED)
                .title(i18n.t("discord.game.title", game.getTitle()))
                .description(i18n.t("discord.game.start"))
                .addField(i18n.t("discord.game.server"), game.getServerName(), false)
                .addField(i18n.t("discord.game.komi"), String.valueOf(game.getKomi()), true)
                .addField(i18n.t("discord.game.handicap"), String.valueOf(game.getHandicap()), true)
                .addField(i18n.t("discord.game.goban"), game.getGobanSize(), true)
                .timestamp(Instant.now());

        if (game.hasUrl()) {
            embed.url(game.getUrl());
        }

        return embed.build();
    }

    @EventListener
    public void onApplicationEvent(RankUpAnnouncementEvent event) {
        announceRankUp(event.member());
    }

    public void announceRankUp(Member member) {
        if (isDisabled()) {
            return;
        }

        EmbedCreateSpec embed = createRankUpAnnouncementEmbed(member);

        String userPing = String.format("<@%d>", member.getDiscordId());
        String content = i18n.t("discord.rankup.message", userPing);

        MessageCreateSpec message = MessageCreateSpec.builder()
                .content(content)
                .addEmbed(embed)
                .build();

        client.getChannelById(configuration.getRankUpAnnouncementChannelId())
                .ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.createMessage(message))
                .subscribe();
    }

    private EmbedCreateSpec createRankUpAnnouncementEmbed(Member member) {
        String title = i18n.t(
                "discord.rankup.title",
                member.getDisplayName(),
                member.getFfgRankHybrid()
        );

        EmbedCreateSpec.Builder embed = EmbedCreateSpec.builder()
                .color(Color.SUMMER_SKY)
                .title(title)
                .thumbnail(member.getDiscordAvatarUrl());

        if (member.hasFfgRankHybrid()) {
            String ffgUrl = ffgWebsite.getProfileUrl(member.getFfgId());
            String ffgLinkTitle = i18n.t("discord.rankup.linktitle");
            String ffgLink = String.format("[%s](%s)", ffgLinkTitle, ffgUrl);
            embed.addField(i18n.t("discord.rankup.profile"), ffgLink, false);
        }

        return embed.build();
    }

    private Member getMemberForEvent(DeferrableInteractionEvent event) {
        discord4j.core.object.entity.Member discordMember = event.getInteraction().getMember().get();

        return getMember(discordMember);
    }

    private Member getMember(discord4j.core.object.entity.Member discordMember) {
        long discordId = discordMember.getId().asLong();
        Member member = memberRepository.findByDiscordId(discordId);

        if (member == null) {
            member = new Member();
            member.setDiscordId(discordId);

            String discriminator = discordMember.getDiscriminator();

            if (!"0".equals(discriminator)) {
                member.setDiscordDiscriminator(discriminator);
            }

            memberRepository.save(member);
        }

        member.setDiscordUsername(discordMember.getUsername());
        member.setDiscordDisplayName(discordMember.getDisplayName());
        member.setDiscordAvatarUrl(discordMember.getAvatarUrl());
        member.setInClub(isMemberInClub(discordMember));
        member.setAdmin(isMemberAdmin(discordMember));

        memberRepository.save(member);

        return member;
    }

    private boolean isMemberInClub(discord4j.core.object.entity.Member discordMember) {
        return discordMember.getRoleIds()
                .contains(configuration.getDiscordMemberRoleId());
    }

    private boolean isMemberAdmin(discord4j.core.object.entity.Member discordMember) {
        return discordMember.getRoleIds()
                .contains(configuration.getDiscordAdminRoleId());
    }

    public void registerCommands() {
        if (isDisabled()) {
            return;
        }

        List<ApplicationCommandRequest> requests = new ArrayList<>();

        for (DiscordCommand command : commands) {
            log.info(String.format("Registering command %s", command.getName()));
            requests.add(command.getRequest());
        }

        client.getRestClient().getApplicationService()
                .bulkOverwriteGuildApplicationCommand(
                        configuration.getApplicationId(),
                        configuration.getGuildId(),
                        requests
                ).subscribe();
    }
}
