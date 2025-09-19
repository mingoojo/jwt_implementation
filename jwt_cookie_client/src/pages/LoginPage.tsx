import { useState } from "react";
import { Box, Button, TextField, Typography, Paper } from "@mui/material";
import { useNavigate } from "react-router-dom"
import UserController from "@/controller/UserController";
import setEncodeLocalStorage from "@/libs/setEncodeLocalStorage";

export default function LoginPage() {

  const navigate = useNavigate()

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");


  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const res = await UserController.postLogin({ email, password })

      if (res.status === 200) {
        setEncodeLocalStorage({ key: "email", value: res.data.email })
        setEncodeLocalStorage({ key: "accessToken", value: res.data.accessToken })
        setEncodeLocalStorage({ key: "role", value: res.data.role })

        navigate("/")
      }
    } catch (error) {
      console.error(error)
    }
  };

  return (
    <Box
      display="flex"
      justifyContent="center"
      alignItems="center"
      minHeight="100vh"
      bgcolor="#f7f7f7"
    >
      <Paper
        elevation={3}
        sx={{
          p: 4,
          width: "100%",
          maxWidth: 400,
          borderRadius: 3,
        }}
      >
        <Typography variant="h5" fontWeight="bold" mb={3} textAlign="center">
          로그인
        </Typography>
        <form onSubmit={handleSubmit}>
          <TextField
            label="이메일"
            type="text"
            variant="outlined"
            fullWidth
            margin="normal"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <TextField
            label="비밀번호"
            type="password"
            variant="outlined"
            fullWidth
            margin="normal"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <Button
            type="submit"
            variant="contained"
            color="primary"
            fullWidth
            sx={{ mt: 2, py: 1.2, fontWeight: "bold", borderRadius: 2 }}
          >
            로그인
          </Button>
          <Button
            variant="outlined"
            color="primary"
            onClick={() => navigate("/signup")}
            fullWidth
            sx={{ mt: 2, py: 1.2, fontWeight: "bold", borderRadius: 2 }}
          >
            회원가입
          </Button>
        </form>
      </Paper>
    </Box>
  );
}