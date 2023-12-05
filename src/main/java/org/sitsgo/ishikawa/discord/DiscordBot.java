package org.sitsgo.ishikawa.discord;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.*;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;
import org.reactivestreams.Publisher;
import org.sitsgo.ishikawa.discord.command.DiscordButtonCommand;
import org.sitsgo.ishikawa.discord.command.DiscordCommand;
import org.sitsgo.ishikawa.discord.command.DiscordMenuCommand;
import org.sitsgo.ishikawa.discord.command.DiscordModalCommand;
import org.sitsgo.ishikawa.goserver.Game;
import org.sitsgo.ishikawa.member.Member;
import org.sitsgo.ishikawa.member.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component("bot")
public class DiscordBot {
    private static final Logger log = LoggerFactory.getLogger(DiscordBot.class);
    private GatewayDiscordClient client;

    @Value("${discord.application-id}")
    private long applicationId;

    @Value("${discord.guild-id}")
    private long guildId;

    @Value("${discord.token}")
    private String token;

    @Value("${discord.game-announcement-channel-id}")
    private long gameAnnouncementChannelId;

    @Value("${discord.member-role-id}")
    private Snowflake discordMemberRoleId;

    @Autowired
    private List<DiscordCommand> commands;

    @Autowired
    private List<DiscordModalCommand> modalCommands;

    @Autowired
    private List<DiscordButtonCommand> buttonCommands;

    @Autowired
    private MessageSource messageSource;

    private final MemberRepository memberRepository;

    public DiscordBot(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void login() {
        client = DiscordClientBuilder.create(token)
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
        return event.reply(messageSource.getMessage("discord.error.notfound", null, Locale.FRANCE))
                .withEphemeral(true);
    }

    public void announceGame(Game game) {
        if (isDisabled()) {
            return;
        }

        EmbedCreateSpec embed = createGameAnnouncementEmbed(game);

        client.getChannelById(Snowflake.of(gameAnnouncementChannelId))
                .ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.createMessage(embed))
                .subscribe();
    }

    private EmbedCreateSpec createGameAnnouncementEmbed(Game game) {
        EmbedCreateSpec.Builder embed = EmbedCreateSpec.builder()
                .color(Color.RED)
                .title("⚔️ " + game.getTitle())
                .description("Une partie vient de commencer !")
                .addField("Serveur", game.getServerName(), false)
                .addField("Komi", String.valueOf(game.getKomi()), true)
                .addField("Handicap", String.valueOf(game.getHandicap()), true)
                .addField("Goban", game.getGobanSize(), true)
                .timestamp(Instant.now());

        if (game.hasUrl()) {
            embed.url(game.getUrl());
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
            String discriminator = null;

            if (!discordMember.getDiscriminator().equals("0")) {
                discriminator = discordMember.getDiscriminator();
            }

            member.setDiscordDiscriminator(discriminator);

            memberRepository.save(member);
        }

        boolean isInClub = discordMember.getRoleIds().contains(discordMemberRoleId);

        member.setDiscordUsername(discordMember.getUsername());
        member.setDiscordDisplayName(discordMember.getDisplayName());
        member.setDiscordAvatarUrl(discordMember.getAvatarUrl());
        member.setInClub(isInClub);

        memberRepository.save(member);

        return member;
    }

    public void registerCommands() {
        if (isDisabled()) {
            return;
        }

        List<ApplicationCommandRequest> requests = new ArrayList<ApplicationCommandRequest>();

        for (DiscordCommand command : commands) {
            log.info(String.format("Registering command %s", command.getName()));
            requests.add(command.getRequest());
        }

        client.getRestClient().getApplicationService()
                .bulkOverwriteGuildApplicationCommand(applicationId, guildId, requests)
                .subscribe();
    }
}
