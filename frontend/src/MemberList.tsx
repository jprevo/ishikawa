import "@inovua/reactdatagrid-community/index.css";
import "@inovua/reactdatagrid-community/theme/blue-dark.css";
import "./scss/grid.scss";
import ReactDataGrid from "@inovua/reactdatagrid-community";
import {
  TypeColumn,
  TypeDataSource,
  TypeFilterValue,
} from "@inovua/reactdatagrid-community/types";
import { useEffect, useState } from "react";
import rankSort from "./member-list/rank-sort.ts";
import Endpoint from "./endpoint.ts";
import { Member } from "./member-list/member.ts";
import CellRender from "./member-list/cell-render.tsx";

function MemberList() {
  const [dataSource, setDataSource] = useState<TypeDataSource>();

  useEffect(() => {
    const loadMembers = async () => {
      const response: Response = await fetch(Endpoint.MemberList);
      const list: Member[] = await response.json();

      setDataSource(list);
    };

    loadMembers();
  }, []);

  const columns: TypeColumn[] = [
    {
      name: "discordAvatarUrl",
      header: "Avatar",
      defaultWidth: 60,
      render: ({ data }) => {
        return CellRender.avatar(data);
      },
    },
    {
      name: "discordDisplayName",
      header: "Discord",
      defaultFlex: 1,
      render: ({ data }) => {
        return CellRender.discordName(data);
      },
    },
    {
      name: "ffgName",
      header: "Nom (FFG)",
      defaultFlex: 1,
      render: ({ data }) => {
        return CellRender.name(data);
      },
    },
    {
      name: "ffgRankHybrid",
      header: "FFG",
      sort: rankSort,
      defaultWidth: 120,
      render: ({ data }) => {
        return CellRender.rank(data);
      },
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
    {
      name: "admin",
      header: "Admin",
      defaultWidth: 90,
      render: ({ value }) => (value ? "Oui" : "Non"),
    },
    {
      name: "kgsUsername",
      header: "KGS",
      defaultWidth: 100,
    },
    {
      name: "ogsUsername",
      header: "OGS",
      defaultWidth: 100,
    },
    {
      name: "foxUsername",
      header: "Fox",
      defaultWidth: 100,
    },
    {
      name: "igsUsername",
      header: "IGS",
      defaultWidth: 100,
    },
    {
      name: "tygemUsername",
      header: "Tygem",
      defaultWidth: 100,
    },
  ];

  if (!dataSource) {
    return <>Chargement...</>;
  }

  const filters: TypeFilterValue = [
    {
      name: "discordDisplayName",
      type: "string",
      operator: "startsWith",
      value: "",
    },
    { name: "ffgName", type: "string", operator: "startsWith", value: "" },
    { name: "kgsUsername", type: "string", operator: "startsWith", value: "" },
    { name: "ogsUsername", type: "string", operator: "startsWith", value: "" },
    { name: "foxUsername", type: "string", operator: "startsWith", value: "" },
    { name: "igsUsername", type: "string", operator: "startsWith", value: "" },
    {
      name: "tygemUsername",
      type: "string",
      operator: "startsWith",
      value: "",
    },
  ];

  return (
    <ReactDataGrid
      columns={columns}
      dataSource={dataSource}
      theme="blue-dark"
      rowHeight={60}
      className="grid"
      defaultFilterValue={filters}
    ></ReactDataGrid>
  );
}

export default MemberList;
