// TODO: 로컬스토리지의 값을 삭제
// TODO:   사용법 - localStorage.removeItem("키이름")

import axiosDefault from "@/utils/axiosDefaultConfig";

const login = (user) => {
  let temp = {
    email: user.email, // 로그인ID
    password: user.password,
  };
  return axiosDefault.post("/auth/login", temp);
};

const logout = () => {
  localStorage.removeItem("user");
};

const register = (user) => {
  let temp = {
    email: user.email,       // 로그인 ID
    password: user.password,
    name: user.name,
    codeName: user.codeName, // 권한
  };
  return axiosDefault.post("/auth/register", temp);
};

const AuthService = {
  login,
  logout,
  register,
};

export default AuthService;