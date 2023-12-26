import "@inovua/reactdatagrid-community/index.css";
import "@inovua/reactdatagrid-community/theme/blue-dark.css";
import "./scss/grid.scss";
import ReactDataGrid from "@inovua/reactdatagrid-community";
import { TypeColumn } from "@inovua/reactdatagrid-community/types";
import { useCallback } from "react";

interface Data {
  discordDisplayName: string;
  ffgName?: string;
  ffgRankHybrid?: string;
  ffgRankMain?: string;
}

interface LoadedResponse {
  data: Data[];
  count: number;
}

const load = async (): Promise<LoadedResponse> => {
  const response: Response = await fetch("/api/member/list");
  const data: Data[] = await response.json();

  return {
    data,
    count: data.length,
  };
};

function MemberList() {
  const dataSource = useCallback(load, []);

  const rankSort = (p1: string, p2: string): number => {
    if (!p1) {
      return 1;
    }

    if (!p2) {
      return -1;
    }

    if (p1.endsWith("d") && p2.endsWith("k")) {
      return -1;
    }

    if (p1.endsWith("k") && p2.endsWith("d")) {
      return 1;
    }

    const p1NumericalRank: number = parseInt(
      p1.substring(0, p1.length - 1),
      10,
    );
    const p2NumericalRank: number = parseInt(
      p2.substring(0, p1.length - 1),
      10,
    );

    if (p1.endsWith("k")) {
      return p1NumericalRank < p2NumericalRank ? -1 : 1;
    }

    if (p1.endsWith("d")) {
      return p1NumericalRank > p2NumericalRank ? -1 : 1;
    }

    return 1;
  };

  const columns: TypeColumn[] = [
    {
      name: "discordAvatarUrl",
      header: "Avatar",
      defaultWidth: 60,
      render: ({ value }) => {
        return <img src={value} className="avatar" />;
      },
    },
    {
      name: "discordDisplayName",
      header: "Discord",
      defaultFlex: 1,
      render: ({ data }) => {
        return (
          <>
            <div>{data.discordDisplayName}</div>
            <small>{data.discordUsername}</small>
          </>
        );
      },
    },
    {
      name: "ffgName",
      header: "Nom (FFG)",
      defaultFlex: 1,
      render: ({ data }) => {
        if (!data.ffgId) {
          return data.ffgName;
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
    },
    {
      name: "ffgRankHybrid",
      header: "FFG Hybride",
      sort: rankSort,
      defaultWidth: 120,
    },
    {
      name: "ffgRankMain",
      header: "Principale",
      defaultWidth: 90,
      sort: rankSort,
    },
    {
      name: "inClub",
      header: "Membre",
      defaultWidth: 90,
      render: ({ value }) => (value ? "Oui" : "Non"),
    },
    {
      name: "anonymous",
      header: "Anonyme",
      defaultWidth: 90,
      render: ({ value }) => (value ? "Oui" : "Non"),
    },
  ];

  return (
    <ReactDataGrid
      columns={columns}
      dataSource={dataSource}
      theme="blue-dark"
      rowHeight={60}
      className="grid"
    ></ReactDataGrid>
  );
}

export default MemberList;
