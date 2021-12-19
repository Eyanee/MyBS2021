<template>
  <div class="app-container">
    <!-- 按钮组 -->
    <div class="button-group">
      <div class="button-group-item">
        <el-upload
          class="upload-demo"
          name="file"
          multiple
          accept="text/plain"
          :headers="{ 'annotate-system-token': token }"
          action="http://localhost:8000/api/annotate_text/upload/"
          :on-success="handleSuccess"
          :on-error="handleError"
          :show-file-list="false"
        >
          <el-button type="primary">
            打开文件夹
          </el-button>
        </el-upload>
      </div>
    </div>
    <div>
      <!--  -->
      <ui-marker
        ref="aiPanel-editor"
        class="ai-observer"
        :unique-key="uuid"
        :ratio="16/9"
        :read-only="readOnly"
        :img-url="currentImage"
      />
    </div>
  </div>
</template>

<script>
import {
  deleteAnnotateTextApi,
  updateAnnotateTextInfoApi,
  removeAllAnnotateTextApi
} from '../../api/Text'
import { getToken } from '@/utils/auth'
import { mapGetters } from 'vuex'
import { AIMarker } from 'vue-picture-bd-marker'

export default {
  name: 'ImgAnnotate',
  filters: {
    statusFilter(status) {
      const statusMap = {
        已标注: 'success',
        未标注: 'gray'
      }
      return statusMap[status]
    }
  },
  components: { 'ui-marker': AIMarker },
  data() {
    return {
      searchTarget: '描述', // 搜索对象
      uuid: '0da9130',
      readOnly: false,
      currentImage: '../../assets/404_images/404',
      keywords: '', // 搜索关键词
      filterList: [], // 符合条件的数据
      list: [], // 所有数据列表
      listLoading: true, // 加载效果
      showEditForm: false, // 编辑框的显隐
      listEditIndex: 0, // 编辑索引
      handleItemId: 0, // 操作条目的id
      form: {
        // 编辑框数据
        description: '',
        text: ''
      }
    }
  },
  computed: {
    // 编辑框宽度
    width() {
      return window.innerWidth <= 400 ? '80%' : '30%'
    },
    token() {
      return getToken()
    },
    ...mapGetters(['annotateTextList'])
  },
  mounted() {
    this.list = this.filterList = this.annotateTextList
    this.listLoading = false
  },
  methods: {
    /**
     * 上传成功的回调函数
     * @param response 响应数据
     */
    handleSuccess(response) {
      this.$message.success('上传成功')
      this.$store.dispatch('user/setAnnotateTextList', response.data)

      this.filterList = this.list = response.data
    },
    /**
     * 上传失败回调函数
     */
    handleError() {
      this.$message.error('上传失败')
    },
    /**
     * 导出数据
     */
    //  TODO 导出数据
    exportData() {
    },
    /**
     * 清空文本数据
     */
    removeAll() {
      this.$confirm('确定要删除吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        removeAllAnnotateTextApi(this.token)
        this.$store.dispatch('user/setAnnotateTextList', [])
        this.list = []
        this.filterList = []
      })
    },

    /**
     * 搜索文本
     */
    search() {
      const keywords = this.keywords.trim()

      if (this.searchTarget === '描述') {
        this.filterList = this.list.filter((item) =>
          item.description.includes(keywords)
        )
      } else {
        this.filterList = this.list.filter((item) =>
          item[this.searchTarget].includes(keywords)
        )
      }
    },
    /**
     * 表格样式
     */
    tableRowClassName({ row, rowIndex }) {
      row
      if (rowIndex % 2) {
        return 'highlight-row'
      }
      return ''
    },
    /**
     * 前往标注页面
     */
    goToAnnotate(index) {
      // 设置标注文本
      this.$store.dispatch(
        'annotate/setAnnotateText',
        this.filterList[index].text
      )
      this.$router.push('/entityAnnotate')
    },
    /**
     * 编辑
     * @param {object} row 操作当前行数据
     */
    handleEdit(index, row) {
      this.showEditForm = true
      this.listEditIndex = index
      this.form.description = row.description
      this.form.text = row.text
      this.handleItemId = row.id
    },
    /**
     * 更新数据
     */
    update() {
      this.filterList[this.listEditIndex].description = this.form.description
      this.filterList[this.listEditIndex].text = this.form.text
      updateAnnotateTextInfoApi(this.filterList[this.listEditIndex])
      this.list.forEach((item) => {
        if (item.id === this.handleItemId) {
          item.description = this.form.description
          item.text = this.form.text
        }
      })
      this.showEditForm = false
    },
    /**
     * 删除文本
     */
    handleDelete(index) {
      const id = this.filterList[index].id
      deleteAnnotateTextApi(id)
      this.filterList.splice(index, 1)
      for (let i = 0; i < this.list.length; i++) {
        if (this.list[i].id === id) {
          this.list.splice(i, 1)
          this.$store.dispatch('user/setAnnotateTextList', this.list)
        }
      }
    }
  }
}
</script>
<style lang="scss">
// 按钮组
.button-group {
  width: 100%;
  display: inline;
  margin: 0 20px 20px 20px;

  &-item {
    margin-right: 20px;
    display: inline-block;
  }
}

// 清空按钮
.clear {
  float: right;
  margin-bottom: 20px;
}

// 搜索框
.search {
  margin-bottom: 20px;

  .el-select {
    width: 150px;
  }
}

// 表格样式
.el-table .highlight-row {
  background: #ebf0fa;
}

// 单行显示文本
.single-line {
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
</style>
