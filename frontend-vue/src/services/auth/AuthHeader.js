// TODO: 웹토큰을 헤더에 넣어 벡엔드로 보내기 위한 헤더 정의
// TODO: 1) 쿠키 대신 로컬스토리지(html 5버전)
// TODO:   => 로컬스토리지 : 쿠키 확장판, 저장공간
// TODO:      사용법 : localStorage.getItem('키');
// TODO: 2) JSON.parse(문자열객체) => 실제객체로 바꾸어주는 함수
// TODO: 3) user 객체 속성 : email(로그인ID), password(암호화), name(이름), accessToken(웹토큰)
// TODO: 4) 헤더 : Bearer (웹토큰 전송시 약속)

// TODO: 결론 : 헤더에 웹토근을 넣어 보내면 벡엔드에서 웹토큰 인증함

export default function AuthHeader() {
  let user = JSON.parse(localStorage.getItem("user"));

  if (user && user.accessToken) {
    return { Authorization: "Bearer " + user.accessToken }; // 웹토큰 전송
  } else {
    return {}; // 웹토큰 미전송
  }
}