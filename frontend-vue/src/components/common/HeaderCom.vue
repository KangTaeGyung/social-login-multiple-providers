<template>
  <div>
    <!-- 부트스트랩 메뉴 넣기 -->
    <nav class="navbar navbar-expand-lg bg-light">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">Simple</a>
        <!-- 단축메뉴(햄버거 메뉴) -->
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <!-- 메뉴(왼쪽) -->
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <!-- 1st : Home -->
            <li class="nav-item">
              <a class="nav-link active" aria-current="page" href="#">Home</a>
            </li>
            <!-- 기초예제 메뉴 #1 -->
            <li class="nav-item dropdown">
              <!-- 대메뉴 -->
              <a
                class="nav-link dropdown-toggle"
                href="#"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                기초 예제
              </a>
              <!-- 소메뉴 -->
              <ul class="dropdown-menu">
                <li><a class="dropdown-item" href="/dept">Dept</a></li>
                <li><a class="dropdown-item" href="/add-dept">Add Dept</a></li>
                <li><hr class="dropdown-divider" /></li>
                <li>
                  <a class="dropdown-item" href="/emp">Emp(연습)</a>
                </li>
                <li>
                  <a class="dropdown-item" href="/add-emp">Add Emp(연습)</a>
                </li>
              </ul>
            </li>
            <!-- 고급예제 메뉴 #2 -->
            <li class="nav-item dropdown">
              <!-- 대메뉴 -->
              <a
                class="nav-link dropdown-toggle"
                href="#"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                고급 예제
              </a>
              <!-- 소메뉴 -->
              <ul class="dropdown-menu">
                <li><a class="dropdown-item" href="/fileDb">FileDb</a></li>
                <li>
                  <a class="dropdown-item" href="/add-fileDb">Add FileDb</a>
                </li>
                <li><hr class="dropdown-divider" /></li>
                <li>
                  <a class="dropdown-item" href="/gallery">Gallery(연습)</a>
                </li>
                <li>
                  <a class="dropdown-item" href="/add-gallery"
                    >Add Gallery(연습)</a
                  >
                </li>
              </ul>
            </li>
          </ul>

          <!-- 메뉴(오른쪽) -->
          <ul class="navbar-nav">
            <!-- {/* 관리자 시작 */} -->
            <li v-if="showAdminBoard" class="nav-item dropdown">
              <a
                class="nav-link dropdown-toggle"
                href="#"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                어드민 예제
              </a>
              <ul class="dropdown-menu">
                <li>
                  <a class="dropdown-item" href="/code-category">
                    Code Category List
                  </a>
                </li>
                <li>
                  <a class="dropdown-item" href="/add-code-category">
                    Add Code Category
                  </a>
                </li>
                <li>
                  <hr class="dropdown-divider" />
                </li>
                <li>
                  <a class="dropdown-item" href="/code"> Code </a>
                </li>
                <li>
                  <a class="dropdown-item" href="/add-code"> Add Code </a>
                </li>
              </ul>
            </li>

            <!-- {/* 로그인 시작 */} -->
            <li v-if="this.$store.state.loggedIn == false" class="nav-item">
              <a class="nav-link active" href="/register"> 회원가입 </a>
            </li>
            <li v-if="this.$store.state.loggedIn == false" class="nav-item">
              <a class="nav-link active" href="/login"> 로그인 </a>
            </li>

            <!-- {/* 로그아웃 시작 */} -->
            <li v-if="this.$store.state.loggedIn == true" class="nav-item">
              <a href class="nav-link active" @click.prevent="handleLogout">
                로그아웃
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  </div>
</template>
<script>
// TODO: computed 함수 (복습) : {{ }} 여기나 v-if 조건식 등에 사용
// TODO:   => 특징 : 매개변수가 없고, 무조건 return 값이 존재

import AuthService from "@/services/auth/AuthService";
export default {
  computed: {
    // ROLE_ADMIN 권한이면 관리자 메뉴 보이기
    showAdminBoard() {
      if (this.$store.state.user != null) {
        return this.$store.state.user.codeName == "ROLE_ADMIN";
      }
      return false;
    },
  },
  methods: {
    // 로그아웃
    handleLogout() {
      AuthService.logout();
      //공유저장소 로그아웃 상태 변경
      this.$store.commit("logout");

      this.$router.push("/login");
    },
  },
};
</script>
<style></style>
