<template>
    <div>
        <van-nav-bar
            title="发起审批"
            left-text="返回"
            left-arrow
            @click-left="() => $router.back()"
        />
        <div style="margin: 10px 10px 0 0;border: 0px solid red;">
            <form-create
                :rule="rule"
                :option="option"
                @submit="onSubmit"
            ></form-create>
        </div>
    </div>
</template>

<script>
export default {
    name: "Apply",
    data() {
        return {
            processTemplateprocessTemplate: null,
            rule: [],
            option: {},
            Userid:JSON.parse(localStorage.getItem('xm-user')).id
        };
    },
    created() {
        let processTemplateId = this.$route.params.id;
        this.fetchData(processTemplateId);
    },
    methods:{
        onSubmit(formData) {
            console.log(formData);
            let formShowData = {};
            this.rule.forEach(item => {
                for (let key in formData) {
                    if (key === item.field) {
                        console.log(item.title, formData[key]);
                        formShowData[item.title] = formData[key];
                    }
                }
            });
            let DATA = {
                formData: formData,
                formShowData: formShowData
            };
            console.log(DATA);
            let processFormVo = {
                "processTemplateId": this.processTemplate.id,
                "processTypeId": this.processTemplate.processTypeId,
                "formValues": JSON.stringify(DATA)
            };
            console.log(processFormVo)
            // console.log(this.Userid)
            // this.$request.post('/processApiController/startUp/'+this.Userid).then(res=>{
            //     // this.$router.push({ path: "/list/2" });
            // })
            this.$request({
                url: '/processApiController/startUp/'+this.Userid,
                method:'POST',
                data:processFormVo
            }).then(res=>{
                if (res.code === '200') {  // 表示成功保存
                    this.$message.success('保存成功')
                    this.$router.push({name:"ProcessList"})
                }
            })
        },
        fetchData(processTemplateId) {
            this.$request.get('/processApiController/getProcessTemplate/'+processTemplateId).then(res=>{
                this.processTemplate = res.data;
                this.rule = JSON.parse(this.processTemplate.formProps);
                this.option = JSON.parse(this.processTemplate.formOptions);
            }).then(res=>{
            })
        },

    }
}
</script>

<style scoped>
.el-form {
    .el-form-item {
        /deep/ .el-form-item__label {
            font-size: 18px;
            font-weight: 800;
            color: blue;
        }
    }
}
</style>