<template>
    <div class="container">
        <van-nav-bar
            title="审批详情"
            left-text="返回"
            left-arrow
            @click-left="() => $router.back()"
        />
        <van-list>
            <van-cell>
                <template slot="default">
                    <div class="header-warp">
                        <h4>{{ process.title }}</h4>
                        <p>{{ process.createTime }}</p>
                        <span class="pass" v-if="process.status === 1">审批中</span>
                        <div class="seal-wrap" v-if="process.status === 2">
                            <seal-avatar></seal-avatar>
                        </div>
                        <div class="seal-wrap" v-if="process.status === -1">
                            <seal-avatar title="已拒绝" color="#EB8473"></seal-avatar>
                        </div>
                    </div>
                </template>
            </van-cell>
            <van-cell>
                <template slot="default">
                    <div class="detail-wrap">
                        <div class="item" :key="key.id" v-for="(value,key) in formValues">
<!--                            <h5>{{ value.title }}</h5>-->
<!--                            <p v-html="value"></p>-->
                        </div>
                    </div>
                </template>
            </van-cell>

            <van-cell>
                <template slot="default">
                    <div class="result">
                        <h3>流程</h3>
                        <van-steps direction="vertical" :active="processRecordList.length - 1">
                            <van-step :key="item.id" v-for="item in processRecordList">
                                <h4>{{ item.id }}</h4>
                                <p>{{ item.description }}</p>
                            </van-step>
                        </van-steps>

                    </div>
                </template>
            </van-cell>
        </van-list>

        <div class="notice" v-if="taskId != 0">
            <van-icon name="bullhorn-o" />
            <p>{{ process.title }}</p>
            <span class="pass">{{ process.status === 1 ? '审批中' : process.status === 2 ? '审批通过' : '审批拒绝' }}</span>
        </div>

        <el-button type="success" @click="approve(1)" style="margin-top: 20px">同意</el-button>
        <el-button type="danger" @click="approve(-1)">拒绝</el-button>
        <div class="footer" v-if="isApprove">
            <div class="left-action">
                <div class="action back" @click="() => $router.back()">
                    <van-icon name="revoke" />
                    <span>返回</span>
                </div>
            </div>
<!--            <div class="right-button">-->
<!--                <van-button @click="approve(-1)" type="default" size="small">审批拒绝</van-button>-->
<!--                <span style="margin: 0 4px"></span>-->
<!--                <van-button @click="approve(1)" type="info" size="small">审批通过</van-button>-->
<!--            </div>-->
        </div>
    </div>
</template>
<script>
export default {
    name: "show",
    // components: {
    //     SealAvatar,
    // },
    props: {
        msg: String
    },
    data() {
        return {
            taskId: 0,
            process: {},
            formValues: {},
            processRecordList: [],
            isApprove: false
        };
    },
    created(){
        this.taskId = this.$route.query.taskId;
        let id = this.$route.query.id;
        this.fetchData(id);
    },
    methods: {
        fetchData(id) {
            this.$request.get('/processApiController/show/'+id).then(res=>{
                this.process = res.data.process
                this.formValues = JSON.parse(res.data.processTemplate.formProps)
                console.log(this.formValues)
                this.processRecordList = res.data.processRecordList
                this.isApprove = res.data.isApprove
            })
        },
        approve(status) {
            let approvalVo = {
                processId: this.process.id,
                taskId: this.taskId,
                status: status
            }
            this.$request({
                url:'/processApiController/approve',
                method:'POST',
                data: approvalVo
            }).then(res=>{})
            this.$router.push('list')
        }
    }
}
</script>

<style scoped>
.container {
    padding-bottom: 86px;
    .header-warp {
        position: relative;
        h4 {
            font-size: 16px;
            margin: 5px;
        }
        p {
            font-size: 16px;
            margin: 5px;
        }
        .pass {
            color: #4CB971;
            margin: 5px;
        }
        .refused {
            color: #EB8473;
            margin: 5px;
        }
    }
    .detail-wrap {
        .item {
            h5 {
                color: #838485;
                margin: 5px;
            }
            p {
                color: #1B1F22;
                font-size: 16px;
                margin: 5px;
            }
        }
    }
    .result {
        font-size: 14px;
        h4, p {
            margin: 5px;
            font-size: 14px;
        }
    }
    .seal-wrap {
        position: absolute;
        top: 20px;
        right: 30px;
    }

    .notice {
        display: flex;
        align-items: center;
        width: 100%;
        font-size: 12px;
        padding: 8px 10px;
        background: #FEFBE8;
        position: fixed;
        bottom: 53px;
        z-index: 10;
        p {
            flex: 1;
            margin: 4px;
        }
        .pass {
            color: #07c160;
            margin-right: 20px;
        }
    }
    .footer {
    /*// height: 50px;*/
        padding: 10px;
        background: #F8F8F8;
        display: flex;
        align-items: center;
        position: fixed;
        width: 100%;
        bottom: 0;
        z-index: 10;
        .left-action {
            flex: 1;
            .action {
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;

                span {
                    font-size: 12px;
                    color: #838485;
                }
            }
        }

        .right-button {
            margin-right: 20px;
        }
    }
}

/deep/ .van-cell {
    position: inherit;
    overflow: visible;
}

/deep/ .van-cell__value{
    position: inherit;
    overflow: visible;
}
</style>