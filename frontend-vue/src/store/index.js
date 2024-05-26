// TODO: vuex : 공유속성, 공유함수 등을 정의, 공유저장소
// TODO:   => 모든 컴포넌트가 사용가능

// TODO: 역할
// TODO:   1) 로컬스토리지에서 최초 생성될때 로컬스토리지에서 user 객체 가져옴
// TODO:   2) 유저 객체를 공유하고 로그인 성공여부 속성을 관리함

// TODO: 공유저장소 구조
// TODO:  1) state     : 공유 속성 정의
// TODO:  2) mutations : 속성 변경 함수 정의, setter 함수 역할
// TODO:  3) actions   : 비동기 함수 정의하는 부분

import { createStore } from "vuex";

const user = JSON.parse(localStorage.getItem("user"));

export default createStore({
  state: {
    loggedIn: user ? true : false, // 로그인 여부 속성
    user: user ? user : null,      
  },
  mutations: {
    // 로그인 성공
    loginSuccess(state, user) {
      state.loggedIn = true;
      state.user = user;
    },
    // 로그인 실패
    loginFailure(state) {
      state.loggedIn = false;
      state.user = null;
    },
    // 로그아웃
    logout(state) {
      state.loggedIn = false;
      state.user = null;
    },
    // 회원가입 성공
    registerSuccess(state) {
      state.loggedIn = false;
    },
    // 회원가입 실패 
    registerFailure(state) {
      state.loggedIn = false;
    },
  },
  actions: {},
});
