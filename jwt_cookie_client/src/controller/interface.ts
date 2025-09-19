
export type ItemProps = {
  title: string
  price: number
}

export type LoginResponse = {
  accessToken: string
  email: string
  grantType: string
  role: string
  userId: number
}