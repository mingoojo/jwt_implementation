import CryptoJS from "crypto-js";
const secretKey = import.meta.env.VITE_PUBLIC_LOCALSTORAGE_SECRET_KEY || ""

export default function setEncodeSessionStorage({ key, value }: { key: string, value: string }) {

  const setSessionStorage = ({ keyData, valueData, secret }: { keyData: string, valueData: string, secret: string }) => {
    // 세션스토리지 데이터 입력
    const encryptedKey = CryptoJS.AES.encrypt(JSON.stringify(keyData), secret).toString()
    const encryptedValue = CryptoJS.AES.encrypt(JSON.stringify(valueData), secret).toString()

    sessionStorage.setItem(String(encryptedKey), String(encryptedValue))
  }

  // 중복제거
  if (typeof window !== "undefined") {
    const keys = Object.keys(sessionStorage);

    const selectedKey = keys.filter((data) => {
      try {
        const decrypted = CryptoJS.AES.decrypt(data, secretKey)
        const parse = JSON.parse(decrypted.toString(CryptoJS.enc.Utf8))
        return parse === key
      } catch (error) {

      }
    })

    if (selectedKey.length >= 1) {
      selectedKey.forEach((keyData) => {
        sessionStorage.removeItem(keyData)
      })
      setSessionStorage({ keyData: key, valueData: value, secret: secretKey })
    } else {
      setSessionStorage({ keyData: key, valueData: value, secret: secretKey })
    }
  }


}