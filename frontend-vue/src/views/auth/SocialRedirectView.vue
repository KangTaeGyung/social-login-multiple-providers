<!-- 소셜로그인 리다이렉트 페이지 -->
<template>
  <div >
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: {},
    };
  },
  mounted() {
    let url = new URL(window.location.href);
    console.log(url);

    const urlParams = url.searchParams;                // uri 정보 가져오기
    const accessToken = urlParams.get("accessToken");
    const email = urlParams.get("email");
    const codeName = urlParams.get("codeName");

    this.user = {
      accessToken: accessToken,
      email: email,
      codeName: codeName,
    };

    console.log("social user", this.user);
    
    localStorage.setItem("user", JSON.stringify(this.user));
    this.$store.commit('loginSuccess', this.user);

    this.$router.push("/");
  },
};
</script>
