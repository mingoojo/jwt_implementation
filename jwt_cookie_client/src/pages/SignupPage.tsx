import { useState } from "react";
import {
  Box,
  Button,
  TextField,
  Typography,
  Paper,
  MenuItem,
  Alert,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import UserController from "@/controller/UserController";


export default function SignupPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    user_name: "",
    email: "",
    password: "",
    department: "",
    position: "",
    role: "USER",
  });
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  const handleChange = (key: keyof typeof form) => (e: React.ChangeEvent<HTMLInputElement>) =>
    setForm({ ...form, [key]: e.target.value });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErr(null);
    setLoading(true);
    try {

      const res = await UserController.postSignup({
        username: form.user_name,
        email: form.email,
        password: form.password,
        department: form.department,
        position: form.position,
        role: form.role,
      })

      if (res?.data === "success") {
        navigate("/login");
      }

    } catch (e: any) {
      setErr(e.message ?? "알 수 없는 오류");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh" bgcolor="#f7f7f7">
      <Paper elevation={3} sx={{ p: 4, width: "100%", maxWidth: 400, borderRadius: 3 }}>
        <Typography variant="h5" fontWeight="bold" mb={3} textAlign="center">
          회원가입
        </Typography>

        {err && <Alert severity="error" sx={{ mb: 2 }}>{err}</Alert>}

        <form onSubmit={handleSubmit}>
          <TextField
            label="이름"
            fullWidth
            margin="normal"
            value={form.user_name}
            onChange={handleChange("user_name")}
            required
          />
          <TextField
            label="이메일"
            type="text"
            fullWidth
            margin="normal"
            value={form.email}
            onChange={handleChange("email")}
            required
          />
          <TextField
            label="비밀번호"
            type="password"
            fullWidth
            margin="normal"
            value={form.password}
            onChange={handleChange("password")}
            required
          />
          <TextField
            label="부서"
            fullWidth
            margin="normal"
            value={form.department}
            onChange={handleChange("department")}
          />
          <TextField
            label="직책"
            fullWidth
            margin="normal"
            value={form.position}
            onChange={handleChange("position")}
          />
          <TextField
            select
            label="권한"
            fullWidth
            margin="normal"
            value={form.role}
            onChange={handleChange("role")}
          >
            <MenuItem value="USER">USER</MenuItem>
            <MenuItem value="ADMIN">ADMIN</MenuItem>
            <MenuItem value="ROOT">ROOT</MenuItem>
          </TextField>

          <Button
            type="submit"
            variant="contained"
            fullWidth
            disabled={loading}
            sx={{ mt: 2, py: 1.2, fontWeight: "bold", borderRadius: 2 }}
          >
            {loading ? "가입 중..." : "가입하기"}
          </Button>

          <Button
            variant="outlined"
            fullWidth
            onClick={() => navigate("/login")}
            sx={{ mt: 2, py: 1.2, fontWeight: "bold", borderRadius: 2 }}
          >
            로그인으로 돌아가기
          </Button>
        </form>
      </Paper>
    </Box>
  );
}