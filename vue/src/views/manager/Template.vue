<template>
    <div class="app-container">
        <el-steps :active="stepIndex" finish-status="success">
            <el-step title="基本设置"></el-step>
            <el-step title="表单设置"></el-step>
            <el-step title="流程设置"></el-step>
        </el-steps>

        <div class="tools-div">
            <el-button v-if="stepIndex > 1" icon="el-icon-check" type="primary" size="small" @click="pre()" round>上一步
            </el-button>
            <el-button icon="el-icon-check" type="primary" size="small" @click="next()" round>{{
                stepIndex == 3 ? '提交保存' : '下一步'
                }}
            </el-button>
            <el-button type="primary" size="small" @click="back()">返回</el-button>
        </div>

        <!-- 第一步 -->
        <div v-show="stepIndex == 1" style="margin-top: 20px;">
            <el-form ref="formRef" :model="form" :rules="rules" label-width="150px" size="small" style="padding-right: 40px;">
                <el-form-item label="审批类型" prop="processName">
                    <el-select v-model="form.processTypeId" placeholder="审批类型">
                        <el-option v-for="item in processTypeList" :label="item.name" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="审批名称">
                    <el-input v-model="form.name" autocomplete="off"/>
                </el-form-item>
                <el-form-item label="描述">
                    <el-input v-model="form.description"/>
                </el-form-item>
            </el-form>
        </div>

        <!-- 第二步 -->
        <div v-show="stepIndex == 2" style="margin-top: 20px;">
            <!--表单构建器-->
            <fc-designer class="form-build" ref="designer"/>
        </div>

<!--        &lt;!&ndash; 第三步 &ndash;&gt;-->
        <div v-show="stepIndex == 3" style="margin-top: 20px;">
            <el-upload
                    class="upload-demo"
                    drag
                    action="http://localhost:9090/oaProcessTemplate/uploadProcessDefinition"
                    :before-upload="beforeUpload"
                    :headers="uploadHeaders"
                    :on-success="onUploadSuccess"
            >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将Activiti流程设计文件拖到此处，或<em>点击上传</em></div>
                <div class="el-upload__tip" slot="tip">只能上传zip压缩文件，且不超过2048kb</div>
            </el-upload>
        </div>
    </div>
</template>

<script>

import {store} from "core-js/internals/reflect-metadata";

const defaultForm = {
    id: '',
    name: '',
    formProps: '',
    formOptions: '',
    processDefinitionKey: '',
    processDefinitionPath: '',
    description: ''
}
export default {
    data() {
        return {
            stepIndex: 1,
            form:defaultForm,
            processTemplate: defaultForm,
            uploadHeaders: {
                // 'token': store.getters.token
                'token': JSON.parse(localStorage.getItem('xm-user')).token
            },
            fileList: [],
            rules:{
                processTypeId:[
                    {required:true, message:'请选择审批类型',trigger:'blur'},
                ],
                name:[
                    {required:true, message:'请输入审批名称',trigger:'blur'},
                ]
            },
            processTypeList:[],
        }
    },

    created() {
        this.loadProcessTypeList()
    },

    methods: {
        store() {
            return store
        },
        loadProcessTypeList(){
            this.$request.get('/oaProcessType/selectAll').then(res => {
                if (res.code === '200') {
                    this.processTypeList = res.data
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        // form,
        pre() {
            this.stepIndex -= 1
        },
        //
        next() {
            if (this.stepIndex === 2) {
                this.form.formProps = JSON.stringify(this.$refs.designer.getRule())
                this.form.formOptions = JSON.stringify(this.$refs.designer.getOption())
            }
            if (this.stepIndex === 3) {
                this.saveOrUpdate()
                this.$router.push('OaProcessTemplate')
            }
            this.stepIndex += 1
        },

        // fetchProcessTypeData() {
        //     processTypeApi.findAll().then(response => {
        //         this.processTypeList = response.data
        //     })
        // },
        // fetchDataById(id) {
        //     api.getById(id).then(response => {
        //         this.form = response.data
        //         // 给表单设计器赋值
        //         this.$refs.designer.setRule(JSON.parse(this.form.formProps))
        //         this.$refs.designer.setOption(JSON.parse(this.form.formOptions))
        //         this.fileList = [{
        //             name: this.form.processDefinitionPath,
        //             url: this.form.processDefinitionPath
        //         }]
        //     })
        // },

        saveOrUpdate() {
            this.saveBtnDisabled = true // 防止表单重复提交
            if (!this.form.id) {
                this.saveData()
            } else {
                this.updateData()
            }
        },

        // 新增
        saveData() {
            this.$refs.formRef.validate((valid)=>{
                if (valid){
                    this.$request({
                        url:'/oaProcessTemplate/add',
                        method:'POST',
                        data: this.form
                    }).then(res => {
                        if (res.code === '200') {  // 表示成功保存
                            this.$message.success('保存成功')
                            // this.load(1)
                            // this.fromVisible = false
                        } else {
                            this.$message.error(res.msg)  // 弹出错误的信息
                        }
                    })
                }
            })
            //     .then(res => {
            //     if (res.code === '200') {  // 表示成功保存
            //         this.$message.success('保存成功')
            //         // this.load(1)
            //         // this.fromVisible = false
            //     } else {
            //         this.$message.error(res.msg)  // 弹出错误的信息
            //     }
            // })
            // api.save(this.form).then(response => {
            //     this.$router.push('/processSet/processTemplate')
            // })
        },

        // 根据id更新记录
        updateData() {
            // api.updateById(this.form).then(response => {
            //     this.$router.push('/processSet/processTemplate')
            // })
        },

        // 文件上传限制条件
        beforeUpload(file) {
            const isZip = file.type === 'application/x-zip-compressed'
            const isLt2M = file.size / 1024 / 1024 < 2

            if (!isZip) {
                this.$message.error('文件格式不正确!')
                return false
            }
            if (!isLt2M) {
                this.$message.error('上传大小不能超过 2MB!')
                return false
            }
            return true
        },

        // 上传成功的回调
        onUploadSuccess(res, file) {
            // 填充上传文件列表
            this.form.processDefinitionPath = res.data.processDefinitionPath
            this.form.processDefinitionKey = res.data.processDefinitionKey
            console.log(this.form.processDefinitionPath)
        },

        back() {
            this.$router.push('OaProcessTemplate')
        }
    }
}
</script>