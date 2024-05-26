// RegisterView.vue
// vueInit
<template>
  <div>
    <div class="card mt-5">
      <div class="card-body p-0">
        <!-- Nested Row within Card Body -->
        <div class="row">
          <div class="col-lg-5 bg-register-image"></div>
          <div class="col-lg-7">
            <div class="p-5">
              <div class="text-center">
                <h1 class="h4 text-gray-900 mb-4">회원 가입</h1>
              </div>
              <form class="user" @submit.prevent="register">
                <div class="form-group">
                  <input
                    type="email"
                    class="form-control form-control-user mb-3"
                    placeholder="이메일 입력"
                    name="email"
                    v-model="user.email"
                  />
                </div>
                <div class="form-group row">
                  <div class="col-sm-6 mb-3 mb-sm-0">
                    <input
                      type="password"
                      class="form-control form-control-user mb-3"
                      placeholder="패스워드 입력"
                      name="password"
                      v-model="user.password"
                    />
                  </div>
                  <div class="col-sm-6">
                    <input
                      type="password"
                      class="form-control form-control-user mb-3"
                      id="exampleRepeatPassword"
                      placeholder="Repeat Password"
                      name="repassword"
                    />
                  </div>
                  <div class="form-group">
                    <input
                      type="text"
                      class="form-control form-control-user mb-3"
                      placeholder="이름 입력"
                      name="name"
                      v-model="user.name"
                    />
                  </div>
                  <div class="form-group">
                    <select
                      class="form-select form-control-select form-select-sm mb-3"
                      name="codeName"
                      v-model="user.codeName"
                    >
                      <option value="ROLE_USER">ROLE_USER</option>
                      <option value="ROLE_ADMIN">ROLE_ADMIN</option>
                    </select>
                  </div>
                </div>
                <button
                  type="submit"
                  class="btn btn-primary btn-user w-100 mb-3"
                >
                  Register Account
                </button>
              </form>
              <hr />
              <div class="text-center">
                <a href="/forgot-password"> Forgot Password? </a>
              </div>
              <div class="text-center">
                <a href="/login"> Already have an account? Login! </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import AuthService from '@/services/auth/AuthService';
export default {
    data() {
        return {
            user: {
                email: "",                              // 로그인 ID
                password: "",
                name: "",
                codeName: "ROLE_USER",                  // 권한, 화면에 보일 초기값
            },
        }
    },
    methods: {
        async register() {
            try {
                let response = await AuthService.register(this.user);
                this.$store.commit("registerSuccess");   // 공유저장소 성공여부 변경
                alert("사용자가 등록되었습니다.");

                console.log(response.data);
            } catch(e) {
                this.$store.commit("registerFailure");

                console.log(e);
            }
        }
    },
    // 화면이 뜰때 자동실행
    mounted() {
      if(this.$store.state.loggedIn == true) {     // 로그인 상태이면 회원가입 필요없음
        this.$router.push("/");
      }
    },
}
</script>
<style>
@import "@/assets/css/login.css";    
</style>