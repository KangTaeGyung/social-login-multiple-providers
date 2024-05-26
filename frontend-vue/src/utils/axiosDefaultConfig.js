// axios 기본 설정 : spring ip 주소 설정
import axios from "axios";

export default axios.create({
    baseURL: "http://localhost:8000/api",
    headers: {
        "Content-Type": "application/json"
    }
})