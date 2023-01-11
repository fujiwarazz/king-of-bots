<template>
    <contentFiled>
        <table class="table table-striped table-hover align-items-center justify-center">
            <thead>
                <tr>
                    <th>#对局ID</th>
                    <th>玩家A</th>
                    <th>玩家B</th>
                    <th>结果</th>
                    <th>对战时间</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="record in records" :key="record.rid">
                    <td>{{ record.rid }}</td>
                    <td>
                        <img class="record-user-photo" :src="record.aavatar" />
                        <span v-if="record.loserNickname === record.buserName" style="color:red">{{
                            record.ausername
                        }}</span>
                        <span v-else-if="record.loserNickname === record.ausername" style="color:blue">{{
                            record.ausername
                        }}</span>
                        <span v-else-if="record.winnerNickname == '平局'" style="color:grey">{{ record.ausername }}</span>

                    </td>
                    <td>
                        <img :src="record.bavatar" class="record-user-photo" />
                        <span v-if="record.loserNickname === record.ausername" style="color:red">{{
                            record.buserName
                        }}</span>
                        <span v-else-if="record.loserNickname === record.buserName" style="color:blue">{{
                            record.buserName
                        }}</span>
                        <span v-else-if="record.winnerNickname == '平局'" style="color:grey">{{ record.buserName }}</span>
                    </td>
                    <td>{{ record.winnerNickname }}</td>
                    <td>{{ record.createTime }}</td>

                    <td>
                        <el-button style="margin-right: 5px" @click="seeDetail(record.rid)" type="primary" :icon="Edit"
                            plain><el-icon>
                                <VideoPlay />
                            </el-icon>查看录像</el-button>
                    </td>
                </tr>
            </tbody>
        </table>
        <a-pagination v-model:current="current" v-model:pageSize="pageSize" :total="tot" @change="changePage" />
    </contentFiled>
</template>

<script>
import contentFiled from "@/components/contentFiled.vue";
import { ref } from "vue";
import { provide } from "vue";
import request from "@/util/request";
import { useRouter } from "vue-router";
import { useStore } from 'vuex'
export default {
    components: {
        contentFiled,
    },
    setup() {
        let current = ref(1);
        let tot = ref(0);
        let records = ref([]);
        let pageSize = ref(10)
        let router = useRouter()
        let store = useStore()
        
        request({
            url: "/records/list",
            method: "post",
            data: {
                pageSize: pageSize.value,
                pageNum: current.value,
            },
        }).then((res) => {
            records.value = res.data.data;
            tot.value = res.data.total;
            current.value = res.data.current
            console.log(records.value);
        });
        const changePage = () =>{
            console.log("asdasd"+pageSize.value)
            request({
            url: "/records/list",
            method: "post",
            data: {
                pageSize: pageSize.value,
                pageNum: current.value,
            },
        }).then((res) => {
            records.value = res.data.data;
            tot.value = res.data.total;
            current.value = res.data.current
            console.log(records.value);
        });
        }
        const string2map = map => {
            let g = []
            for (let i = 0, k = 0; i < 13; i++) {
                let s = []
                for (let j = 0; j < 14; j++, k++) {
                    if (map[k] == '1') s.push(1)
                    else s.push(0)
                }
                g.push(s)
            }
            return g
        }
        const seeDetail = (rid) => {
            let record = records.value.filter(item => item.rid == rid)[0]
            provide(record)
            store.commit('updateRecord', true)
            console.log(record)
            let mp = string2map(record.map)
            store.commit("updateRecordLoser", record.loser)
            store.commit("updateGameMap", {
                a_id: record.aid,
                b_id: record.bid,
                a_sx: record.asx,
                a_sy: record.asy,
                b_sx: record.bsx,
                b_sy: record.bsy,
                map: mp
            })
            store.commit("updateStep", {
                a_steps: record.asteps,
                b_steps: record.bsteps
            })
            router.push({
                name: 'recordContent', params: {
                    recordId: rid
                }
            })

        }
        return { current, tot, records, pageSize, seeDetail ,changePage};
    },
};
</script>

<style scoped>
.record-user-photo {
    width: 5vh;
    margin-right: 1vw;
    border-radius: 45%;
}
</style>
