<template>
  <contentFiled>
    <div class="row justify-content-md-center">
      <a-form
        ref="formRef"
        :model="formState"
        name="basic"
        :label-col="{ span: 8 }"
        :wrapper-col="{ span: 8 }"
        autocomplete="off"
        :rules="rules"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
      >
        <a-form-item label="账号" name="username">
          <a-input v-model:value="formState.username">
            <template #prefix
              ><UserOutlined style="color: rgba(0, 0, 0, 0.25)"
            /></template>
          </a-input>
        </a-form-item>

        <a-form-item label="密码" name="password" has-feedback>
          <a-input v-model:value="formState.password" type="password">
            <template #prefix
              ><LockOutlined style="color: rgba(0, 0, 0, 0.25)"
            /></template>
          </a-input>
        </a-form-item>
        <a-form-item label="确认密码" name="checkPassword" has-feedback>
          <a-input v-model:value="formState.checkPassword" type="password">
            <template #prefix
              ><LockOutlined style="color: rgba(0, 0, 0, 0.25)"
            /></template>
          </a-input>
        </a-form-item>
        <a-form-item
          name="avatar"
          label="选择头像"
          :label-col="{ span: 8 }"
          :wrapper-col="{ span: 8 }"
        >
          <a-upload
            v-model:file-list="fileList"
            name="avatar"
            list-type="picture-card"
            class="avatar-uploader"
            :show-upload-list="false"
            action="http://localhost:9999/user/account/avatar"
            :before-upload="beforeUpload"
            @change="handleChange"
          >
            <img v-if="imageUrl" class="img" :src="imageUrl" alt="avatar" />
            <div v-else>
              <loading-outlined v-if="loading"></loading-outlined>
              <plus-outlined v-else></plus-outlined>
              <div class="ant-upload-text">Upload</div>
            </div>
          </a-upload>
        </a-form-item>
        <a-form-item name="remember" :wrapper-col="{ offset: 8, span: 16 }">
          <a-checkbox v-model:checked="formState.remember">记住我?</a-checkbox>
        </a-form-item>

        <a-form-item :wrapper-col="{ offset: 8, span: 26 }">
          <a-button
            
            type="primary"
            :disabled="disabled"
            @click="register"
            html-type="submit"
            >注册~</a-button
          >
        </a-form-item>
      </a-form>
    </div>
  </contentFiled>
</template>

<script>
import contentFiled from "@/components/contentFiled.vue";
import { reactive, ref } from "vue";
import {
  UserOutlined,
  LockOutlined,
  PlusOutlined,
  LoadingOutlined,
} from "@ant-design/icons-vue";
import { message } from "ant-design-vue";
import { computed } from "vue";
import request from '../../../util/request'
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
export default {
  components: {
    contentFiled,
    UserOutlined,
    LockOutlined,
    LoadingOutlined,
    PlusOutlined,
  },
  setup() {
    const formState = reactive({
      username: "",
      password: "",
      checkPassword: "",
      remember: true,
      avatar: "",
    });
    const router = useRouter()
    const onFinish = (values) => {
      console.log("Success:", values);
    };
    const onFinishFailed = (errorInfo) => {
      console.log("Failed:", errorInfo);
    };
    const fileList = ref([]);
    const loading = ref(false);
    const imageUrl = ref("");
    const formRef = ref();
    const handleChange = (info) => {
      if (info.file.status === "uploading") {
        loading.value = true;
        return;
      }
      if (info.file.status === "done") {
        // Get this url from response in real world.
        imageUrl.value = info.file.response.data;
        formState.avatar = imageUrl.value
        console.log(formState.avatar);

      }
      if (info.file.status === "error") {
        loading.value = false;
        message.error("upload error");
      }
    };
    const beforeUpload = (file) => {
      const isJpgOrPng = file.type === "image/jpeg" || file.type === "image/png";
      if (!isJpgOrPng) {
        message.error("You can only upload JPG file!");
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        message.error("Image must smaller than 2MB!");
      }
      return isJpgOrPng && isLt2M;
    };
    const disabled = computed(() => {
      return !(formState.username && formState.password && formState.checkPassword);
    });
    let validateUsername = async (_rule, value) => {
      if (value === "") {
        return Promise.reject("请输入账号!");
      } else if (value.length < 6 || value.length > 13) {
        return Promise.reject("账号长度在6~13位");
      } else {
        if (formState.checkPassword !== "") {
          formRef.value.validateFields("password");
        }
        return Promise.resolve();
      }
    };
    let validatePass = async (_rule, value) => {
      if (value === "") {
        return Promise.reject("请输入密码");
      } else if (value.length < 6 || value.length > 13) {
        return Promise.reject("密码长度在6~13位");
      } else {
        if (formState.checkPassword !== "") {
          formRef.value.validateFields("checkPassword");
        }
        return Promise.resolve();
      }
    };
    let validatePass2 = async (_rule, value) => {
      if (value === "") {
        return Promise.reject("Please input the password again");
      } else if (value.length < 6 || value.length > 13) {
        return Promise.reject("账号长度在6~13位");
      } else if (value !== formState.password) {
        return Promise.reject("两次输入不同!");
      } else {
        return Promise.resolve();
      }
    };
    let register =()=>{
        request({
            url:'/user/account/register',
            method:'post',
            data:formState
        }).then(res=>{
            if(res.code == 200){
                ElMessage.success("注册成功~")
                router.push('/user/account/login')
            }else{
                ElMessage.error(res.errorMessage)
            }
        })
    }
    const rules = {
      username: [
        {
          required: true,
          validator: validateUsername,
          trigger: "change",
        },
      ],
      password: [
        {
          required: true,
          validator: validatePass,
          trigger: "change",
        },
      ],
      checkPassword: [
        {
          required: true,
          validator: validatePass2,
          trigger: "change",
        },
      ],
    };

    return {
      // checkUsername,
      // checkPassword,
      // checkCheckPassword,
      rules,
      register,
      formRef,
      formState,
      onFinish,
      onFinishFailed,
      fileList,
      loading,
      imageUrl,
      handleChange,
      beforeUpload,
      disabled,
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
#components-form-demo-normal-login .login-form-button {
  width: 100%;
}
</style>
