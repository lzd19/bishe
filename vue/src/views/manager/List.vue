<template>
    <div class="container">
        <van-nav-bar
            title="审批列表"
        />
        <van-tabs v-model="activeIndex" @click="tabSwitch">
            <van-tab
                v-for="(item,key) in tabList"
                :key="key"
                :title="item.title"
            >
            </van-tab>
        </van-tabs>

        <div class="list-wrap" >
            <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
                <van-list
                    v-model="loading"
                    :finished="finished"
                    finished-text="没有更多了"
                    @load="onLoad"
                    :immediate-check="false"
                >
                    <van-cell v-for="item in list" :key="item.id" @click="info(item.id, item.taskId)">
                        <template slot="default">
                            <div class="item-wrap">
                                <div class="item-header">
                                    <h3>{{item.title}}</h3>
                                    <span>{{item.createTime}}</span>
                                </div>
                                <div class="item-status">
                                    <span :class="item.status === 1 ? '审批中' : item.status === 2 ? 'pass' : 'refused'">{{ item.status === 1 ? '审批中' : item.status === 2 ? '审批通过' : '审批拒绝' }}</span>
                                </div>
                            </div>
                        </template>
                    </van-cell>
                </van-list>
            </van-pull-refresh>
        </div>
    </div>
</template>

<script>
export default {
    name: "List",
    data() {
        return {
            list: [],
            loading: false,
            finished: false,
            refreshing: false,
            Userid:JSON.parse(localStorage.getItem('xm-user')).id,
            pageNo: 1,
            pageSize: 10,
            pages: 1,

            activeIndex: 0,
            tabList: [
                { title:"待处理", },
                { title:"已处理", },
                { title:"已发起", }
            ]
        };
    },
    created(){
        this.activeIndex = parseInt(this.$route.params.activeIndex);
        this.onLoad()
    },
    methods:{
        tabSwitch() {
            //tab切换，重新初始化数据
            this.list = []
            this.pageNo = 1
            this.finished = false

            //tabs切换时，如果之前的tab已经滚动到底部（list加载到底部），直接点击其他的tab，将再触发一次onload事件。
            //可能调用2次onLoad()方法，延迟执行，通过时间差解决问题
            setTimeout(() => {
                if(!this.finished) {
                    this.onLoad();
                }
            }, 500);
        },
        onLoad() {
            if(this.activeIndex === 0) {
                this.findPending()
            }
            if(this.activeIndex === 1) {
                this.findProcessed()
            }
            if(this.activeIndex === 2) {
                this.findStarted()
            }
        },
        findPending(){
            console.log(this.pageNo);
            this.$request.get('/processApiController/selectPage/'+this.Userid).then(res=>{
                console.log(res.data);
                if (this.refreshing) {
                    this.list = [];
                    this.refreshing = false;
                }
                for (let i=0;i<res.data.list.length;i++) {
                    let item = res.data.list[i]
                    // item.formValues = JSON.parse(item.formValues)
                    this.list.push(item);
                }
                this.pages = res.data.pages;

                this.loading = false;
                if(this.pageNo >= this.pages) {
                    this.finished = true;
                }

                this.pageNo++;
            })
        },
        findProcessed() {
            this.$request.get('/processApiController/findProcessed').then(res=>{
                console.log(res.data);
                if (this.refreshing) {
                    this.list = [];
                    this.refreshing = false;
                }
                for (let i=0;i<res.data.list.length;i++) {
                    let item = res.data.list[i]
                    // item.formValues = JSON.parse(item.formValues)
                    this.list.push(item);
                }
                this.pages = res.data.pages;

                this.loading = false;
                if(this.pageNo >= this.pages) {
                    this.finished = true;
                }

                this.pageNo++;
            })
        },

        findStarted() {
            this.$request.get('/processApiController/findStarted').then(res=>{
                console.log(res.data);
                if (this.refreshing) {
                    this.list = [];
                    this.refreshing = false;
                }
                for (let i=0;i<res.data.list.length;i++) {
                    let item = res.data.list[i]
                    // item.formValues = JSON.parse(item.formValues)
                    this.list.push(item);
                }
                this.pages = res.data.pages;

                this.loading = false;
                if(this.pageNo >= this.pages) {
                    this.finished = true;
                }

                this.pageNo++;
            })
        },
        onRefresh() {
            // 清空列表数据
            this.finished = false;

            this.pageNo = 1;
            // 重新加载数据
            // 将 loading 设置为 true，表示处于加载状态
            this.loading = true;
            this.onLoad();
        },
        info(id, taskId) {
            // this.$router.push({ path: '/show/'+id+'/'+taskId })
            // this.$router.push({ path: 'Show',params:{id:id,taskId:taskId}})
            this.$router.push({ path: 'Show',query:{id:id,taskId:taskId}})
        }
    }
}
</script>

<style scoped>
/deep/ .van-nav-bar {
    background: #1D1E20;
}
/deep/ .van-nav-bar__title {
    color: #fff;
}
.container {
    padding-bottom: 50px;
    .list-wrap {
        margin-top: 4px;
        border-top: 1px solid #ebedf0;
    }
    .item-wrap {
        font-size: 12px;
        color: #A7A8A9;
        .item-header {
            display: flex;
            align-items: center;
            img {
                width: 20px;
                height: 20px;
                border-radius: 4px;
                margin-right: 4px;
            }
            h3 {
                flex: 1;
                font-size: 15px;
                color: #000;
                padding: 0;
                margin: 0;
            }

        }

        .item-block {
            padding: 4px 0;
            font-size: 14px;
            p {
                padding: 0;
                margin: 0;
                line-height: 20px;
            }
        }
        .item-status {
            .pass {
                color: #4CB971;
            }
            .refused {
                color: #EB8473;
            }}}}
</style>