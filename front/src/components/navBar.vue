<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container">
        <router-link class="navbar-brand" :to="{ name: 'dashbord' }"
          >King of Bots</router-link
        >
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarText"
          aria-controls="navbarText"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarText">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <router-link
                class="nav-link"
                :class="name == 'combat' ? 'nav-link active' : 'nav-link'"
                :to="{ name: 'combat' }"
                >对战</router-link
              >
            </li>
            <li class="nav-item">
              <router-link
                class="nav-link"
                :class="name == 'record' ? 'nav-link active' : 'nav-link'"
                :to="{ name: 'record' }"
                >对局列表</router-link
              >
            </li>
            <li class="nav-item">
              <router-link
                class="nav-link"
                :class="name == 'rank' ? 'nav-link active' : 'nav-link'"
                :to="{ name: 'rank' }"
                >排行榜</router-link
              >
            </li>
          </ul>
          <ul v-if="$store.state.user.is_login==false" class="navbar-nav">
            <img src="https://picx.zhimg.com/80/v2-6afa72220d29f045c15217aa6b275808_720w.webp?source=1940ef5c" style='border-radius: 45%;' alt="Logo" width="40" height="40" class="d-inline-block align-text-top">
      
            <li class="nav-item dropdown">
              <a
                class="nav-link dropdown-toggle"
                aria-current="page"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
                href="#"
                >游客</a
              >

              <ul class="dropdown-menu">
                <li>
                  <router-link
                    class="dropdown-item"
                    :class="name == 'userBot' ? 'nav-link active' : 'nav-link'"
                    :to="{ name: 'userLogin' }"
                    >登录</router-link
                  >
                </li>
                <li>
                  <router-link
                    class="dropdown-item"
                    :class="name == 'userProfile' ? 'nav-link active' : 'nav-link'"
                    :to="{ name: 'userRegister' }"
                    >注册</router-link
                  >
                </li>
              </ul>
            </li>
          </ul>
          <ul v-else class="navbar-nav">
            <img :src="$store.state.user.avatar" alt="Logo" width="40" height="40" style='border-radius: 45%;' class="d-inline-block align-text-top">

            <li class="nav-item dropdown">
              <a
                class="nav-link dropdown-toggle"
                aria-current="page"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
                href="#"
                >{{$store.state.user.nickname}}</a
              >

              <ul class="dropdown-menu">
                <li>
                  <router-link
                    class="dropdown-item"
                    :class="name == 'userBot' ? 'nav-link active' : 'nav-link'"
                    :to="{ name: 'userBot' }"
                    >myBots</router-link
                  >
                </li>
                <li>
                  <router-link
                    class="dropdown-item"
                    :class="name == 'userProfile' ? 'nav-link active' : 'nav-link'"
                    :to="{ name: 'userProfile' }"
                    >myProfile</router-link
                  >
                </li>
                <li><hr class="dropdown-divider" /></li>
                <li><a v-if="!islogin" class="dropdown-item" @click="logOut">Quit</a></li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  </div>
</template>

<script>
import { useRoute, useRouter } from "vue-router";
import { computed, ref, onMounted } from "vue";
import { useStore } from "vuex";
export default {
  setup() {
    let route = useRoute();
    let router = useRouter();
    let store = useStore();
    let islogin = ref(false);

    let nickName = ref("游客");

    onMounted(() => {
      if (localStorage.getItem("user") != null) {
        nickName.value = JSON.parse(localStorage.getItem("user")).nickname;
      } else if (sessionStorage.getItem("user") != null) {
        nickName.value = JSON.parse(sessionStorage.getItem("user")).nickname;
      } 
    });
    let name = computed(() => route.name);

    const logOut = () => {
      store.dispatch("logout", {
        success() {
          router.push("/user/account/login");
        },
      });
    };
    return {
      logOut,
      name,
      nickName,
      islogin,
    };
  },
};
</script>

<style scoped></style>
