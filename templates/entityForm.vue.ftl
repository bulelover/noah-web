<template>
  <div style="height: 100%;">
    <div class="form-container" style="width: 900px" v-loading="loading">
      <div class="form-title">
        <div class="page-title">
          {{ title }}
          <el-link v-if="!isAdd" @click="refreshData" icon="el-icon-refresh" :underline="false">刷新</el-link>
        </div>
        <div>
          <el-button :disabled="loading" v-if="!isView" type="primary" @click="save" :loading="saveLoading">保存</el-button>
          <el-button :disabled="loading || saveLoading" @click="close">取消</el-button>
        </div>
      </div>
      <el-form class="form-body" ref="form" :model="form" :rules="rules" label-width="120px" :disabled="isView" v-if="!loading">
          <#-- ----------  BEGIN 字段循环遍历  ---------->
          <#list table.fields as field>
              <#if field.propertyName != 'createTime' && field.propertyName != 'updateTime'
              && field.propertyName != 'createUserId' && field.propertyName != 'updateUserId'
              && field.propertyName != 'createLoginName' && field.propertyName != 'updateLoginName'
              && field.propertyName != 'createRealName' && field.propertyName != 'updateRealName'
              && field.propertyName != 'flag' && field.propertyName != 'id'>
        <el-form-item class="w-ib w-1-2" label="${(field.comment!?length gt 0)?then(field.comment,'未定义')}" prop="${field.propertyName}">
          <#if ((field.comment!?length gt 0)?then(field.comment,'未定义'))?contains('select')>
          <el-select v-model="form.${field.propertyName}" placeholder="请选择${(field.comment!?length gt 0)?then(field.comment,'未定义')}" clearable>
            <el-option v-for="item in G.getDictList('')" :key="item.code"
                       :label="item.name" :value="item.code"></el-option>
          </el-select>
          <#elseif ((field.comment!?length gt 0)?then(field.comment,'未定义'))?contains('date')>
          <el-date-picker v-model="form.${field.propertyName}" type="date" placeholder="请选择${(field.comment!?length gt 0)?then(field.comment,'未定义')}" clearable>
          </el-date-picker>
          <#elseif ((field.comment!?length gt 0)?then(field.comment,'未定义'))?contains('switch')>
          <el-switch v-model="form.${field.propertyName}" active-text="正常" inactive-text="锁定" active-value="1"
                     inactive-value="0"></el-switch>
          <#else >
          <el-input v-model="form.${field.propertyName}" placeholder="请输入${(field.comment!?length gt 0)?then(field.comment,'未定义')}"
                    maxlength="20"></el-input>
          </#if>
        </el-form-item>
              </#if>
          </#list>
          <#------------  END 字段循环遍历  ---------->
      </el-form>
    </div>
  </div>
</template>

<script>
  import ${entity}Api from './'
  export default {
    data() {
      return {
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
          ${field.propertyName}: G.validator.required('blur','${(field.comment!?length gt 0)?then(field.comment,'')}不能为空',
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
      },
      isAdd: {
        get() {
          return this.page === 'add';
        }
      }
    },
    methods: {
      init(page, obj) {
        this.title = '新增${tableDesc!}';
        this.page = page;
        //编辑、查看
        if (!this.isAdd) {
          this.id = obj.id;
          this.title = this.isView ? '查看${tableDesc!}' : '修改${tableDesc!}';
          this.refreshData();
        }
      },
      refreshData(){
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
        this.$parent.editVisible = false;
      },
      refreshTable() {
        this.$emit('refreshTable');
      }
    }
  }
</script>

<style scoped lang="scss">

</style>
