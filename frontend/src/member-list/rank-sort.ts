function rankSort(p1: string, p2: string): number {
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

  const p1NumericalRank: number = parseInt(p1.substring(0, p1.length - 1), 10);
  const p2NumericalRank: number = parseInt(p2.substring(0, p2.length - 1), 10);

  if (p1.endsWith("k")) {
    return p1NumericalRank < p2NumericalRank ? -1 : 1;
  }

  if (p1.endsWith("d")) {
    return p1NumericalRank > p2NumericalRank ? -1 : 1;
  }

  return 1;
}

export default rankSort;
