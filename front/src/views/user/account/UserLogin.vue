<template>
  <contentFiled>
    <div class="row justify-content-md-center">
      <a-form
        :model="formState"
        name="basic"
        :label-col="{ span: 8 }"
        :wrapper-col="{ span: 8 }"
        autocomplete="off"
        @submit.prevent="login"
      >
        <a-form-item
          label="用户名"
          name="username"
          :rules="[{ required: true, message: '请输入用户名!' }]"
        >
          <a-input v-model:value="formState.username">
            <template #prefix
              ><UserOutlined style="color: rgba(0, 0, 0, 0.25)"
            /></template>
          </a-input>
        </a-form-item>

        <a-form-item
          label="密码"
          name="password"
          :rules="[{ required: true, message: '请输入密码!' }]"
        >
          <a-input v-model:value="formState.password" type="password">
            <template #prefix
              ><LockOutlined style="color: rgba(0, 0, 0, 0.25)"
            /></template>
          </a-input>
        </a-form-item>

        <a-form-item name="remember" :wrapper-col="{ offset: 8, span: 16 }">
          <a-checkbox v-model:checked="formState.remember">记住我?</a-checkbox>
        </a-form-item>

        <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
          <a-button type="primary" html-type="submit">登录~</a-button>
        </a-form-item>
      </a-form>
    </div>
  </contentFiled>
</template>

<script>
import contentFiled from "@/components/contentFiled.vue";
import { reactive } from "vue";
import { UserOutlined, LockOutlined } from "@ant-design/icons-vue";
import { useStore } from "vuex";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
export default {
  components: {
    contentFiled,
    UserOutlined,
    LockOutlined,
  },
  setup() {
    const formState = reactive({
      username: "",
      password: "",
      remember: true,
    });

    const store = useStore();

    const router = useRouter();

    const login = () => {
      store.dispatch("login", {
        nickname: formState.username,
        password: formState.password,
        success(resp) {
          console.log(resp);

          if (formState.remember == true) {
            store.dispatch("getInfo", {
                success(res) {
                localStorage.setItem("user", JSON.stringify(res.data));
                localStorage.setItem("token", JSON.stringify(resp.data));

                ElMessage({
                  message: "登录成功!",
                  type: "success",
                  duration: 1000,
                });
                router.push("/home");
              },
              error(){
                ElMessage({
                  message: "登陆失败!",
                  type: "error",
                  duration: 1000,
                });
              }
              
            });

          } else {

            store.dispatch("getInfo", {
              success(res) {
                sessionStorage.setItem("user", JSON.stringify(res.data));
                sessionStorage.setItem("token", JSON.stringify(resp.data));

                ElMessage({
                  message: "登录成功!",
                  type: "success",
                  duration: 1000,
                });
                router.push("/home");
              },
              error(){
                ElMessage({
                  message: "系统错误!",
                  type: "error",
                  duration: 1000,
                });
              }
            });
          }
        },
        error(resp) {
          ElMessage({
            message: `${resp.errorMessage}!`,
            type: "error",
            duration: 1000,
          });
        },
      });
    };

    return {
      formState,

      login,
    };
  },
};
</script>
<style scoped>
.avatar-uploader > .ant-upload {
  width: 102px;
  height: 102px;
}
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
.img {
  height: 102px;
  width: 102px;
}
</style>
