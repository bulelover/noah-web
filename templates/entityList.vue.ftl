<template>
	<div class="page-wrapper">
		<div class="white-box full-height">
			<span class="page-title mb16">${tableDesc!}列表</span>
			<div class="search-flex mb16">
				<div class="search-group">
					<el-form label-position="top" :model="searchForm" @keyup.enter.native="refreshList"
					         @submit.native.prevent>
						<el-input v-model="searchForm.search" placeholder="模糊搜索（需后台实现）" style="width: 220px"></el-input>
						<el-button icon="el-icon-search" @click="refreshList"></el-button>
					</el-form>
				</div>
				<div>
					<el-button v-if="$perms.has('${permission!}-add')" type="primary" @click="add">新增</el-button>
				</div>
			</div>
			<el-table ref="table" border :data="tableData" v-loading="tableLoading" row-key="id"
								:height="g.tableHeight">
				<el-table-column label="序号" width="50" type="index" align="center" show-overflow-tooltip
				                 :index="(searchForm.current-1)*searchForm.size+1"></el-table-column>
	<#-- ----------  BEGIN 字段循环遍历  ---------->
	<#list table.fields as field>
		<#if field_index gt 0>
			<#if field_index == 1>
				<el-table-column prop="${field.propertyName}" label="${(field.comment!?length gt 0)?then(field.comment,'未定义')}" min-width="120" show-overflow-tooltip>
					<template v-slot="{row}">
						<el-link type="primary" v-if="$perms.has('${permission!}-view')"
										 :underline="false" @click="view(row)">{{ row.${field.propertyName} }}
						</el-link>
						<template v-else>{{ row.${field.propertyName} }}</template>
					</template>
				</el-table-column>
			</#if>
			<#if field_index != 1>
				<el-table-column prop="${field.propertyName}" label="${(field.comment!?length gt 0)?then(field.comment,'未定义')}" min-width="120" show-overflow-tooltip></el-table-column>
			</#if>
		</#if>
	</#list>
	<#------------  END 字段循环遍历  ---------->
				<el-table-column label="操作" width="200" class-name="link-menu" fixed="right"
          v-if="$perms.has('${permission!}-edit') || $perms.has('${permission!}-delete')">
					<template v-slot="{row}">
						<el-link v-if="$perms.has('${permission!}-edit')" :underline="false" type="primary" @click="edit(row)">
							修改
						</el-link>
						<el-link v-if="$perms.has('${permission!}-delete')" :underline="false" type="danger" @click="del(row)">
							删除
						</el-link>
					</template>
				</el-table-column>
			</el-table>
			<div class="pagination">
				<el-pagination layout="total, sizes, prev, pager, next, jumper" :total="total"
				               :current-page.sync="searchForm.current"
				               :page-sizes="[10, 20, 50, 100]"
				               :page-size.sync="searchForm.size"
				               @size-change="refreshList"
				               @current-change="fetchData"
				></el-pagination>
			</div>
		</div>
    <${entity}Form v-if="editVisible" ref="edit" @refreshTable="fetchData"></${entity}Form>
  </div>
</template>
<script>
  import ${entity}Form from "./${entity}Form";
	import ${entity}Api from './';
  export default {
    components: {
      ${entity}Form
    },
		data() {
			return {
				searchForm: {
					search: '',
					size: 10,
					current: 1,
					// orders: [{
					//   column: 'code',
					//   type: 'asc'
					// }, {
					//   column: 'code2',
					//   type: 'asc'
					// }]
				},
				tableLoading: false,
				total: 0,
				editVisible: false,
				tableData: [],
			}
		},
		created() {
			this.refreshList();
		},
		activated() {
			if (this.$refs.table) {
				this.$refs.table.doLayout();
			}
		},
		methods: {
			refreshList() {
				if (this.$refs.table) {
					this.$refs.table.doLayout();
				}
				this.searchForm.current = 1;
				this.fetchData();
			},
			fetchData() {
				this.tableLoading = true;
				${entity}Api.page({
					data: {
						...this.searchForm
					},
					callback: data => {
						this.tableData = data.records;
						this.total = data.total
					},
					complete: () => {
						this.tableLoading = false;
					}
				});
			},
			add() {
				this.editVisible = true;
				this.$nextTick(() => {
          this.$refs.edit.init('add');
				})
			},
			edit(row) {
				this.editVisible = true;
				this.$nextTick(() => {
          this.$refs.edit.init('edit', {id: row.id});
				})
			},
			view(row) {
				this.editVisible = true;
				this.$nextTick(() => {
					this.$refs.edit.init('view', {id: row.id});
				})
			},
			del(row) {
				this.$confirm2('确定要删除这条数据吗？').then(() => {
          ${entity}Api.removeById({
            data: {
              id: row.id
            },
            callback: (d, msg) => {
              this.$message.success(msg);
              this.fetchData()
            },
          })
				}).catch(_ => _)
			},
		}

  }
</script>

<style scoped lang="scss">

</style>

