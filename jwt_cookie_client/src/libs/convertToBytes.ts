export default function convertToBytes(sizeStr: string): string {

  if (typeof sizeStr === "string") {
    const units: Record<string, number> = {
      B: 1,
      K: 1024,
      M: 1024 ** 2,
      G: 1024 ** 3,
      T: 1024 ** 4,
      P: 1024 ** 5,
    };

    const regex = /^([\d.]+)\s*([KMGT]?)(B)?$/i;
    const match = sizeStr.trim().match(regex);

    if (!match) {
      return "0";
    }

    const [, valueStr, unit] = match;
    const value = parseFloat(valueStr);
    const multiplier = units[unit.toUpperCase()] || 1;

    return String(Math.round(value * multiplier));
  } else {
    // console.log(sizeStr, "sizeStr error")
    return "0"
  }

}