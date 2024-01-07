import { Member } from "./member.ts";
import { ReactElement } from "react";
import Endpoint from "../endpoint.ts";

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
  name: (
    data: Member,
    loadMembers: CallableFunction,
  ): ReactElement | string => {
    let ffgReloadButton: ReactElement = <></>;

    const reloadFfg = async () => {
      const url: string = Endpoint.ReloadFFG.replace(
        "{id}",
        data.id.toString(10),
      );
      const response: Response = await fetch(url);
      await response.json();

      loadMembers();
    };

    if (!data.ffgId) {
      return data.ffgName ?? "";
    } else {
      const lastCheck = data.ffgLastCheck ? (
        <>&nbsp;({data.ffgLastCheck})</>
      ) : null;

      ffgReloadButton = (
        <>
          <br />
          <small>
            <a href="#" onClick={reloadFfg} className="secondary">
              Recharger le profil FFG{lastCheck}
            </a>
          </small>
        </>
      );
    }

    const url: string =
      "https://ffg.jeudego.org/php/affichePersonne.php?id=" +
      data.ffgId.toString(10);

    return (
      <>
        <a href={url} target="_blank">
          {data.ffgName}
        </a>
        {ffgReloadButton}
      </>
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
