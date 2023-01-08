<template>
  <div class="container">
    <div class="row" style="margin-top: 30px">
      <div class="col-3">
        <div class="card">
          <div class="card-body">
            <img :src="$store.state.user.avatar" alt="" style="width: 100%" />
            <v-divider inset></v-divider>
            <div
              class="row"
              style="text-align: center; font-size: 18px; font-weight: bold"
            >
              <el-divider content-position="right"><span color="red">
                {{
                $store.state.user.nickname
              }}
              </span></el-divider>
            </div>
            <v-divider inset></v-divider>
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card">
          <div class="card-header">
            <span style="font-size: 120%">我的Bots</span>
            <v-btn
              class="float-end"
              variant="flat"
              color="#409EFF"
              data-bs-toggle="modal"
              data-bs-target="#createBot"
            >
              <el-icon>
                <Position />
              </el-icon>
              创建Bot</v-btn
            >
          </div>

          <!-- Modal -->
          <div
            class="modal fade"
            id="createBot"
            tabindex="-1"
            aria-labelledby="exampleModalLabel"
            aria-hidden="true"
          >
            <div class="modal-dialog modal-xl">
              <div class="modal-content">
                <div class="modal-header">
                  <h1 class="modal-title fs-5" id="exampleModalLabel">在这里填入信息</h1>
                  <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="modal"
                    aria-label="Close"
                  ></button>
                </div>
                <div class="modal-body" style="font-size: 16px">
                  <form>
                    <div class="mb-3">
                      <label for="botTitle" class="form-label">名称</label>
                      <input
                        type="email"
                        class="form-control"
                        id="botTitle"
                        aria-describedby="emailHelp"
                        placeholder="请输入bot简介"
                        v-model="botInfo.title"
                      />
                      <div id="emailHelp" class="form-text">
                        给你的Bot取个好听的名字吧~
                      </div>
                    </div>
                    <div class="mb-3">
                      <label for="BotDesc" class="form-label">简介</label>
                      <textarea
                        type="password"
                        class="form-control"
                        id="BotDesc"
                        placeholder="请输入bot简介"
                        v-model="botInfo.description"
                      />
                    </div>
                    <div class="mb-3">
                      <label for="BotCode" class="form-label">代码</label>
                      <!-- <textarea type="password" class="form-control" id="BotCode"
                                                placeholder="请输入代码" rows="7" v-model="botInfo.code" /> -->

                      <VAceEditor
                        v-model:value="botInfo.code"
                        @init="editorInit"
                        lang="c_cpp"
                        theme="textmate"
                        style="height: 300px"
                      />
                    </div>
                    <div class="mb-3 form-check">
                      <input
                        type="checkbox"
                        class="form-check-input"
                        id="open"
                        v-model="botInfo.isOpen"
                      />
                      <label class="form-check-label" for="open">是否公开代码</label>
                    </div>
                  </form>
                </div>
                <div class="modal-footer">
                  <v-btn color="red" data-bs-dismiss="modal"> 关闭 </v-btn>
                  <v-btn color="green" @click="addBot" data-bs-dismiss="modal">
                    保存
                  </v-btn>
                </div>
              </div>
            </div>
          </div>

          <div class="card-body">
            <table
              class="table table-striped table-hover align-items-center justify-center"
            >
              <thead>
                <tr>
                  <th>#BotId</th>
                  <th>Bot名称</th>
                  <th>创建时间</th>
                  <th>是否公开</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="bot in myBots" :key="bot.bid">
                  <td>{{ bot.bid }}</td>
                  <td>{{ bot.btitle }}</td>
                  <td>{{ bot.createTime }}</td>
                  <td v-if="bot.bisOpen == 1">
                    <el-tag class="ml-2" size="large" type="success">公开</el-tag>
                  </td>
                  <td v-else>
                    <el-tag class="ml-2" size="large" type="danger">不公开</el-tag>
                  </td>

                  <td>
                    <el-button
                      style="margin-right: 5px"
                      data-bs-toggle="modal"
                      :data-bs-target="'#updateBot' + bot.bid"
                      type="primary"
                      :icon="Edit"
                      plain
                      ><el-icon> <Edit /> </el-icon>修改</el-button
                    >

                    <!-- Modal -->
                    <div
                      class="modal fade"
                      :id="'updateBot' + bot.bid"
                      tabindex="-1"
                      aria-labelledby="exampleModalLabel"
                      aria-hidden="true"
                    >
                      <div class="modal-dialog modal-xl">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h1 class="modal-title fs-5" id="exampleModalLabel">
                              在这里填入信息
                            </h1>
                            <button
                              type="button"
                              class="btn-close"
                              data-bs-dismiss="modal"
                              aria-label="Close"
                            ></button>
                          </div>
                          <div class="modal-body" style="font-size: 16px">
                            <form>
                              <div class="mb-3">
                                <label for="botTitle" class="form-label">名称</label>
                                <input
                                  type="email"
                                  class="form-control"
                                  id="botTitle"
                                  aria-describedby="emailHelp"
                                  placeholder="请输入bot简介"
                                  v-model="bot.btitle"
                                />
                                <div id="emailHelp" class="form-text">
                                  给你的Bot取个好听的名字吧~
                                </div>
                              </div>
                              <div class="mb-3">
                                <label for="BotDesc" class="form-label">简介</label>
                                <textarea
                                  type="password"
                                  class="form-control"
                                  id="BotDesc"
                                  placeholder="请输入bot简介"
                                  v-model="bot.bdesc"
                                />
                              </div>
                              <div class="mb-3">
                                <label for="BotCode" class="form-label">代码</label>
                                <!-- <textarea
                                  type="password"
                                  class="form-control"
                                  id="BotCode"
                                  placeholder="请输入代码"
                                  rows="7"
                                  v-model="bot.bcode"
                                /> -->

                                <VAceEditor
                                  v-model:value="bot.bcode"
                                  @init="editorInit"
                                  lang="c_cpp"
                                  theme="textmate"
                                  style="height: 300px"
                                />
                              </div>
                              <div class="mb-3 form-check">
                                <input
                                  type="checkbox"
                                  class="form-check-input"
                                  id="open"
                                  v-model="bot.isOpen"
                                />
                                <label class="form-check-label" for="open"
                                  >是否公开代码</label
                                >
                              </div>
                            </form>
                          </div>
                          <div class="modal-footer">
                            <v-btn color="red" data-bs-dismiss="modal"> 关闭 </v-btn>
                            <v-btn
                              color="green"
                              @click="updateBot(bot)"
                              data-bs-dismiss="modal"
                            >
                              保存
                            </v-btn>
                          </div>
                        </div>
                      </div>
                    </div>

                    <el-button @click="removeBot(bot.bid)" type="danger" plain
                      ><el-icon> <Delete /> </el-icon>删除</el-button
                    >
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import request from "@/util/request";
import { ElMessage, ElMessageBox } from "element-plus";
import { Modal } from "bootstrap/dist/js/bootstrap";
import { VAceEditor } from "vue3-ace-editor";
import ace from "ace-builds";

export default {
  components: { VAceEditor },
  setup() {
    ace.config.set(
      "basePath",
      "https://cdn.jsdelivr.net/npm/ace-builds@" +
        require("ace-builds").version +
        "/src-noconflict/"
    );

    // const store = useStore();
    let user = JSON.parse(localStorage.getItem("user"));
    let myBots = ref([]);
    let botInfo = reactive({
      description: "",
      code: "",
      title: "",
      isOpen: false,
    });
    const getBots = () => {
      request({
        url: `/bot/getList/${user.userId}`,
        method: "get",
      }).then((res) => {
        myBots.value = res.data;
      });
    };

    const updateBot = (bot) => {
      request({
        url: `/bot/updateBot`,
        method: "post",
        data: {
          description: bot.bdesc,
          code: bot.bcode,
          title: bot.btitle,
          isOpen: bot.isOpen,
          userId: bot.kid,
          id: bot.bid,
        },
      }).then((res) => {
        if (res.code == 200) {
          ElMessage.success("修改成功!");
          getBots();
        } else {
          ElMessage.error(`${res.errorMessage}!`);
        }
      });
    };
    const removeBot = (id) => {
      ElMessageBox.confirm("确认删除这个bot吗?", "Warning", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          request({
            url: `/bot/deleteBot/${id}`,
            method: "post",
          }).then((res) => {
            if (res.code == 200) {
              ElMessage.success("删除成功!");
              getBots();
            } else {
              ElMessage.error(`${res.errorMessage}!`);
            }
          });
        })
        .catch(() => {
          ElMessage({
            type: "info",
            message: "取消删除",
          });
        });
    };

    const addBot = () => {
      request({
        url: "/bot/add",
        method: "post",
        data: {
          description: botInfo.description,
          code: botInfo.code,
          title: botInfo.title,
          isOpen: botInfo.isOpen ? 1 : 0,
        },
      }).then((res) => {
        if (res.code == 200) {
          ElMessage.success("添加成功!");
          getBots();
          (botInfo.code = ""),
            (botInfo.description = ""),
            (botInfo.title = ""),
            (botInfo.isOpen = false);
          Modal.getInstance("#createBot").hide();
        } else {
          ElMessage.error(`${res.errorMessage}!`);
        }
      });
    };
    getBots();

    return {
      getBots,
      myBots,
      botInfo,
      addBot,
      removeBot,
      updateBot,
    };
  },
};
</script>

<style scoped></style>
