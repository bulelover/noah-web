<template>
  <el-dialog :title="title"
             :append-to-body="true"
             :width="G.dialogWidth2"
             :visible.sync="visible"
             :close-on-click-modal="false"
             :close-on-press-escape="false"
             :before-close="close"
             v-dialog-drag>
    <div class="form-container" v-loading="loading">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px" :disabled="isView">
          <#-- ----------  BEGIN 字段循环遍历  ---------->
          <#list table.fields as field>
              <#if field.propertyName != 'createTime' && field.propertyName != 'updateTime'
              && field.propertyName != 'createUserId' && field.propertyName != 'updateUserId'
              && field.propertyName != 'createLoginName' && field.propertyName != 'updateLoginName'
              && field.propertyName != 'createRealName' && field.propertyName != 'updateRealName'
              && field.propertyName != 'flag' && field.propertyName != 'id'>
        <el-form-item class="w-ib w-1-2" label="${(field.comment!?length gt 0)?then(field.comment,'未定义')}" prop="${field.propertyName}">
          <el-input v-model="form.${field.propertyName}" placeholder="请输入${(field.comment!?length gt 0)?then(field.comment,'未定义')}"></el-input>
        </el-form-item>
              </#if>
          </#list>
          <#------------  END 字段循环遍历  ---------->
      </el-form>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button :disabled="loading" v-if="!isView" type="primary" @click="save" :loading="saveLoading">保存</el-button>
      <el-button :disabled="loading || saveLoading" @click="close">取消</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import ${entity}Api from './'
  export default {
    data() {
      return {
        visible: false,
        loading: false,
        saveLoading: false,
        id: '',
        title: '',
        page: 'view',
        rules: {
          <#list table.fields as field>
          <#if field.propertyName != 'createTime' && field.propertyName != 'updateTime'
          && field.propertyName != 'createUserId' && field.propertyName != 'updateUserId'
          && field.propertyName != 'createLoginName' && field.propertyName != 'updateLoginName'
          && field.propertyName != 'createRealName' && field.propertyName != 'updateRealName'
          && field.propertyName != 'flag' && field.propertyName != 'id'>
          ${field.propertyName}: [{required: false, trigger: 'blur', message: '${(field.comment!?length gt 0)?then(field.comment,'')}不能为空'}],
          </#if>
          </#list>
        },
        form: {
          <#list table.fields as field>
          <#if field.propertyName != 'createTime' && field.propertyName != 'updateTime'
          && field.propertyName != 'createUserId' && field.propertyName != 'updateUserId'
          && field.propertyName != 'createLoginName' && field.propertyName != 'updateLoginName'
          && field.propertyName != 'createRealName' && field.propertyName != 'updateRealName'
          && field.propertyName != 'flag'>
          ${field.propertyName}: ${(field.propertyType=='String')?string('\'\'', 'null')},
          </#if>
          </#list>
        },
      }
    },
    computed: {
      isView: {
        get() {
          return this.page === 'view';
        }
      }
    },
    methods: {
      init(page, obj) {
        this.title = '新增${tableDesc!}';
        this.visible = true;
        this.id = obj.id;
        this.page = page;
        //编辑、查看
        if (this.page === 'edit' || this.page === 'view') {
          this.title = this.isView ? '查看${tableDesc!}' : '修改${tableDesc!}';
          this.loading = true;
          ${entity}Api.getById({
            data: {
              id: this.id
            },
            callback: d => {
              this.form = d;
            },
            complete: () => {
              this.loading = false;
            }
          });
        }
      },
      save() {
        let opts = {
          data: this.form,
          callback: (d, msg) => {
            this.$message.success(msg);
            this.refreshTable();
            this.close();
          },
          complete: () => {
            this.saveLoading = false;
          }
        };
        this.$refs.form.validate((valid) => {
          if (valid) {
            if (this.page === 'add') {
              this.saveLoading = true;
              ${entity}Api.add(opts);
            }
            if (this.page === 'edit') {
              this.saveLoading = true;
              ${entity}Api.edit(opts);
            }
          } else {
            return false
          }
        })
      },
      close() {
        this.visible = false;
        //延时销毁form表单
        setTimeout(() => {
          this.$parent.editVisible = false;
        }, G.destroyTimeout)
      },
      refreshTable() {
        this.$emit('refreshTable');
      }
    }
  }
</script>

<style scoped lang="scss">

</style>
