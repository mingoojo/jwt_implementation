export function formatBytes(
  bytes: number,
  {
    base = 1024,
    decimals = 1,
    space = false,
    lowercase = true,
  }: { base?: 1000 | 1024; decimals?: number; space?: boolean; lowercase?: boolean } = {}
): string {
  if (!Number.isFinite(bytes)) {
    return "0b";
  }

  const sign = bytes < 0 ? "-" : "";
  let val = Math.abs(bytes);

  const units = ["B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
  if (val === 0) {
    return lowercase ? "0b" : "0 B";
  }

  let i = 0;
  while (val >= base && i < units.length - 1) {
    val /= base;
    i++;
  }

  // 고정 자릿수 → 0 꼬리 제거(예: "6.0" → "6")
  const fixed = val.toFixed(decimals);
  const trimmed = decimals > 0 ? fixed.replace(/\.0+$|(\.\d*?[1-9])0+$/, "$1") : fixed;

  const unit = lowercase ? units[i] : units[i];
  return `${sign}${trimmed}${space ? " " : ""}${unit}`;
}