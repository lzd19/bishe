<template>
    <div>
        <div class="operation">
            <el-button type="primary" plain @click="handleAdd">新增</el-button>
            <el-button type="danger" plain @click="delBatch">批量删除</el-button>
        </div>

        <div class="table">
            <el-table :data="tableData" strip @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="id" label="序号" width="70" align="center" sortable></el-table-column>
                <el-table-column prop="name" label="审批名称"></el-table-column>
                <el-table-column prop="processName" label="审批类型"></el-table-column>
                <el-table-column prop="description" label="描述"></el-table-column>
                <el-table-column prop="createTime" label="创建时间"></el-table-column>
                <el-table-column prop="updateTime" label="更新时间"></el-table-column>
                <el-table-column label="操作" align="center" width="180">
                    <template v-slot="scope">
                        <el-button type="text" size="mini" @click="show(scope.row)">查看审批设置</el-button>
                        <el-button size="mini" type="text" plain @click="handleEdit(scope.row)">编辑</el-button>
                        <el-button size="mini" type="text" plain @click="del(scope.row.id)">删除</el-button>
                        <el-button v-if="scope.row.status == 0" type="text" size="mini" @click="publish(scope.row.id)">发布</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <div class="pagination">
                <el-pagination
                        background
                        @current-change="handleCurrentChange"
                        :current-page="pageNum"
                        :page-sizes="[5, 10, 20]"
                        :page-size="pageSize"
                        layout="total, prev, pager, next"
                        :total="total">
                </el-pagination>
            </div>
        </div>

        <el-dialog title="查看审批设置" :visible.sync="formDialogVisible" width="35%">
            <h3>基本信息</h3>
            <el-divider/>
            <el-form ref="flashPromotionForm" label-width="150px" size="small" style="padding-right: 40px;">
                <el-form-item label="审批类型" style="margin-bottom: 0px;">{{ processTemplate.processName }}</el-form-item>
                <el-form-item label="名称" style="margin-bottom: 0px;">{{ processTemplate.name }}</el-form-item>
                <el-form-item label="创建时间" style="margin-bottom: 0px;">{{ processTemplate.createTime }}</el-form-item>
            </el-form>
            <h3>表单信息</h3>
            <el-divider/>
            <div>
                <form-create
                        :rule="rule"
                        :option="option"
                ></form-create>
            </div>
            <span slot="footer" class="dialog-footer">
    <el-button @click="formDialogVisible = false" size="small">取 消</el-button>
  </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "User",
    data() {
        return {
            tableData: [],  // 所有的数据
            pageNum: 1,   // 当前的页码
            pageSize: 10,  // 每页显示的个数
            total: 0,
            username: null,
            fromVisible: false,
            form: {},
            user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
            rules: {
                username: [
                    {required: true, message: '请输入账号', trigger: 'blur'},
                ]
            },
            ids: [],
            processName: [],

            rule: [],
            option: {},
            processTemplate: {},
            formDialogVisible: false
        }
    },
    created() {
        this.load(1)
        this.loadProcessName()
    },
    methods: {
        publish(id) {
            // api.publish(id).then(response => {
            //     this.$message.success('发布成功')
            //     this.fetchData(this.page)
            // })
            this.$request.get('/oaProcessTemplate/publish/' + id).then(res => {
                if (res.code === '200'){
                    this.$message.success('发布成功')
                    this.load(1)
                }else {
                    this.$message.error(res.msg)
                }
            })
        },
        show(row) {
            this.rule = JSON.parse(row.formProps)
            this.option = JSON.parse(row.formOptions)
            this.processTemplate = row
            this.formDialogVisible = true
        },
        loadProcessName() {
            this.$request.get('/oaProcessTemplate/selectAll').then(res => {
                if (res.code == '200') {
                    this.departmentData = res.data
                } else {
                    this.$message.error(res.msg)
                }
            })
        },
        handleAdd() {   // 新增数据
            this.$router.push('Template')
            // this.form = {}  // 新增数据的时候清空数据
        },
        handleEdit(row) {   // 编辑数据
            this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
            this.fromVisible = true   // 打开弹窗
        },
        save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
            this.$refs.formRef.validate((valid) => {
                if (valid) {
                    this.$request({
                        url: this.form.id ? '/oaProcessTemplate/update' : '/oaProcessTemplate/add',
                        method: this.form.id ? 'PUT' : 'POST',
                        data: this.form
                    }).then(res => {
                        if (res.code === '200') {  // 表示成功保存
                            this.$message.success('保存成功')
                            this.load(1)
                            this.fromVisible = false
                        } else {
                            this.$message.error(res.msg)  // 弹出错误的信息
                        }
                    })
                }
            })
        },
        del(id) {   // 单个删除
            this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
                this.$request.delete('/oaProcessTemplate/delete/' + id).then(res => {
                    if (res.code === '200') {   // 表示操作成功
                        this.$message.success('操作成功')
                        this.load(1)
                    } else {
                        this.$message.error(res.msg)  // 弹出错误的信息
                    }
                })
            }).catch(() => {
            })
        },
        handleSelectionChange(rows) {   // 当前选中的所有的行数据
            this.ids = rows.map(v => v.id)
        },
        delBatch() {   // 批量删除
            if (!this.ids.length) {
                this.$message.warning('请选择数据')
                return
            }
            this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
                this.$request.delete('/oaProcessTemplate/delete/batch', {data: this.ids}).then(res => {
                    if (res.code === '200') {   // 表示操作成功
                        this.$message.success('操作成功')
                        this.load(1)
                    } else {
                        this.$message.error(res.msg)  // 弹出错误的信息
                    }
                })
            }).catch(() => {
            })
        },
        load(pageNum) {  // 分页查询
            if (pageNum) this.pageNum = pageNum
            this.$request.get('/oaProcessTemplate/selectPage', {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    // username: this.username,
                }
            }).then(res => {
                this.tableData = res.data?.list
                this.total = res.data?.total
            })
        },
        reset() {
            // this.username = null
            this.load(1)
        },
        handleCurrentChange(pageNum) {
            this.load(pageNum)
        },
    }
}
</script>

<style scoped>

</style>