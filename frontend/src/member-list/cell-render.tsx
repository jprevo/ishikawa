import { Member } from "./member.ts";
import { ReactElement } from "react";

const CellRender = {
  rank: (data: Member): ReactElement => {
    if (!data.ffgRankHybrid && data.ffgRankMain) {
      return <>- ({data.ffgRankMain})</>;
    }

    if (data.ffgRankHybrid && !data.ffgRankMain) {
      return <>{data.ffgRankHybrid}</>;
    }

    return (
      <>
        {data.ffgRankHybrid} ({data.ffgRankMain})
      </>
    );
  },
  name: (data: Member): ReactElement | string => {
    if (!data.ffgId) {
      return data.ffgName ?? "";
    }

    const url: string =
      "https://ffg.jeudego.org/php/affichePersonne.php?id=" +
      data.ffgId.toString(10);

    return (
      <a href={url} target="_blank">
        {data.ffgName}
      </a>
    );
  },
  discordName: (data: Member): ReactElement => {
    return (
      <>
        <div>{data.discordDisplayName}</div>
        <small>{data.discordUsername}</small>
      </>
    );
  },
  avatar: (data: Member): ReactElement => {
    return <img src={data.discordAvatarUrl} className="avatar" />;
  },
};

export default CellRender;
