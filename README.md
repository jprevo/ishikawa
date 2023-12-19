A small app with a Discord Bot and a Web app linked together made for managing
SITS ([Stones In The Shell](https://sits-go.org/)) go/baduk/weiqi club members.

⚠️ **ATM This project is very much WIP and not ready for production** ⚠️

This project must be able to run on a small VPS with limited RAM and vCPU and should build on Java 17/Spring Boot 3.2.0.

## Environment variables

There are several env variables to set before running this project.

### Database

```
DATABASE_URL // should look like : `jdbc:sqlite:var/database.sqlite`.
DATABASE_USERNAME
DATABASE_PASSWORD
```

We use sqlite in order to keep things simple and compact.

### Discord

```
DISCORD_TOKEN // See your discord bot app
DISCORD_APPLICATION_ID // See your discord bot app
DISCORD_GUILD_ID // Server ID
DISCORD_GAME_ANNOUNCEMENT_CHANNEL_ID // Where our bot should make games annoucements?
DISCORD_RANK_UP_ANNOUNCEMENT_CHANNEL_ID // Where our bot should make FFG rank up announcements?
DISCORD_MEMBER_ROLE_ID // The role ID assigned to our paid members
DISCORD_REVIEW_MENTION_ROLE_ID // Which role to ping for reviews

// Webapp sign-in:
DISCORD_OAUTH_CLIENT_ID
DISCORD_OAUTH_CLIENT_SECRET
DISCORD_OAUTH_REDIRECT_URI
```

### KGS

```
KGS_BOT_USERNAME
KGS_BOT_PASSWORD
KGS_CHANNEL_ID
```

In order to retrieve games from the KGS Go Server, we need a bot user to be able to sign in, and the room where games
are played.

## TODO

- [x] Manage account with Discord Bot
- [x] Parse games from KGS and OGS
- [ ] Web UI build with React for administrators (OAuth2 login)
- [x] Discord announcements
- [ ] Manage all messages with Spring Messages
- [ ] Upload SGF on Discord for analysis
- [x] Level-up announcements!