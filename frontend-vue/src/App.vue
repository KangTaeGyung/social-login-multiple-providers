<template>
<!-- 머리말 -->
<HeaderCom />

<!-- 본문 -->
<div class="container">
    <router-view/>
</div>

</template>

<script>
// 상대주소
// import HeaderCom from "./components/common/HeaderCom.vue";
// 절대주소
import HeaderCom from "@/components/common/HeaderCom.vue";

export default {
    components : {
        HeaderCom    // 컴포넌트 등록
    }
}
</script>