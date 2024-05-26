<!-- 사용법 : @submit.prevent="함수" -->
<!--       prevent : submit 의 기본 속성을 막기(다른 곳으로 이동하려는 특징)  -->

<template>
  <div>
    <div class="row justify-content-center">
      <div class="col-xl-10 col-lg-12 col-md-9">
        <div class="card mt-5">
          <div class="card-body p-0">
            <!-- {/* Nested Row within Card Body */} -->
            <div class="row">
              <div class="col-lg-6 bg-login-image"></div>
              <div class="col-lg-6">
                <div class="p-5">
                  <div class="text-center">
                    <h1 class="h4 mb-4">Welcome Back!</h1>
                  </div>
                  <form class="user" @submit.prevent="login">
                    <div class="form-group">
                      <input
                        type="email"
                        class="form-control form-control-user mb-3"
                        placeholder="이메일을 넣기"
                        name="email"
                        v-model="user.email"
                      />
                    </div>
                    <div class="form-group">
                      <input
                        type="password"
                        class="form-control form-control-user mb-3"
                        placeholder="패스워드 넣기"
                        name="password"
                        v-model="user.password"
                      />
                    </div>

                    <button class="btn btn-primary btn-user w-100 mb-3">
                      Login
                    </button>
                    <hr />
                    <a
                      href="http://localhost:8000/oauth2/authorization/google"
                      class="btn btn-google btn-user w-100 mb-2"
                    >
                      <i class="fab fa-google fa-fw"></i>&nbsp;Login with Google
                    </a>
                    <a href="http://localhost:8000/oauth2/authorization/naver" class="btn btn-naver btn-user w-100 mb-2">
                      <i class="fa-solid fa-n"></i>&nbsp;Login with Naver
                    </a>
                    <a href="/" class="btn btn-kakao btn-user w-100 mb-3">
                      <i class="fa-solid fa-k"></i>&nbsp;Login with Kakao
                    </a>
                  </form>
                  <hr />
                  <div class="text-center">
                    <a class="small" href="/forgot-password">
                      Forgot Password?
                    </a>
                  </div>
                  <div class="text-center">
                    <a class="small" href="/register"> Create an Account! </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
// TODO: 1) spring 보내준 user 객체(웹토큰있음)를 로컬스토리지에 저장
// TODO:   사용법 :  localStorage.setItem(키, 값);
// TODO:     => 단, 값은 문자열만 저장됨
// TODO:   사용법 : JSON.stringify(객체) => 문자열로 바뀐 객체가 리턴됨

// TODO: 2) 공유저장소의 state / mutations 함수 접근법
// TODO:   mutations 사용법 : this.$store.commit("함수명", 저장할객체)
// TODO:     => 로그인성공 공유함수(loginSuccess(state, 유저객체)) 실행
// TODO:   state 사용법 : this.$store.state.공유속성명
// TODO:     => 공유저장소의 공유속성 접근법

// TODO: 3) 뷰의 라이프사이클
// TODO:   - mounted() : 화면이 뜰때 자동 실행 (생명주기 함수)
// TODO:   - created() : 뷰가 생성될대 자동 실행
// TODO:   - created()(1번, 뷰만 생성되면 실행) -> mounted()(2번, html 태그까지 모두 뜰때)
// TODO:     예) destoryed() : 뷰가 삭제될때 실행 (거의 사용 않함)

import AuthService from "@/services/auth/AuthService";
export default {
  data() {
    return {
      user: {
        email: "", // 로그인ID
        password: "",
      },
    };
  },
  methods: {
    async login() {
      try {
        let response = await AuthService.login(this.user);
        console.log(response.data);

        localStorage.setItem("user", JSON.stringify(response.data)); // 1)

        this.$store.commit("loginSuccess", response.data); // 2)

        this.$router.push("/");
      } catch (e) {
        this.$store.commit("loginFailure");

        console.log(e);
      }
    },
  },
  // 화면이 뜰때 실행되는 함수
  created() {
    if (this.$store.state.loggedIn == true) {
      // 로그인 상태이면 로그인 불필요
      this.$router.push("/");
    }
  },
};
</script>
<style>
@import "@/assets/css/login.css";
</style>
