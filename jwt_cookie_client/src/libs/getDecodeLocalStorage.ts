import CryptoJS from "crypto-js";

export default function getDecodeLocalStorage({ key }: { key: string }) {
  const secretKey = import.meta.env.VITE_PUBLIC_LOCALSTORAGE_SECRET_KEY || ""

  if (typeof window !== "undefined") {
    const keys = Object.keys(localStorage);

    const selectedKey = keys.filter((data) => {
      try {
        const decrypted = CryptoJS.AES.decrypt(data, secretKey)
        const parse = JSON.parse(decrypted.toString(CryptoJS.enc.Utf8))
        return parse === key
      } catch (error) {

      }
    })

    if (selectedKey.length === 1) {
      try {
        const Encodedvalue = localStorage.getItem(selectedKey[0])
        const decrypted = CryptoJS.AES.decrypt(String(Encodedvalue), secretKey)
        const parse = JSON.parse(decrypted.toString(CryptoJS.enc.Utf8))
        return { value: parse }
      } catch (error) {
        return { value: "" }
      }
    } else if (selectedKey.length > 1) {
      localStorage.clear()
    }
  }
}
