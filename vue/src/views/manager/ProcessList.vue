<template>
    <div>
        <ul v-for="item in list">
            {{item.name}}
            <li v-for="i in item.processTemplateList">
                <div @click="apply(i.id)">{{i.name}}</div>
            </li>
        </ul>
    </div>
</template>

<script>
export default {
    name: "ProcessList",
    data() {
        return {
            list: [],
        };
    },
    created() {
        this.fetchData();
    },
    methods: {
        fetchData() {
            this.$request.get('/processApiController/findProcessType').then(res => {
                console.log(res.data);
                this.list = res.data;
            })
        },
        apply(id){
            // this.$router.push({ path: '/apply/'+id });
            this.$router.push({name:'Apply',params:{id:id}})
        }
    }
}
</script>

<style scoped>
    ul{
        margin: 50px;
        font-size: 30px;
    }
    li{
        margin-top: 10px;
        text-indent: 50px;
        list-style-type: none;
        color: #2a60c9;
    }
</style>