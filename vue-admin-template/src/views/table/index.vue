<template>
  <div>
    <el-card shadow="hover" class="mgb20" style="height:500px;">
      <div class="video">
        <el-upload
          class="upload-demo"
          drag
          action="http://localhost:8080/uploadPics"
          multiple
        >
          <i class="el-icon-upload" />
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div slot="tip" class="el-upload__tip">只能上传mp4文件，且不超过50M</div>
        </el-upload>
      </div>
      <div class="user-info">
        <el-upload
          ref="upload"
          class="upload-demo"
          action="http://localhost:8080/uploadPics"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :file-list="fileList"
          :auto-upload="false"
        >
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
        </el-upload>
      </div>
    </el-card>
  </div>
</template>
<script>
export default {
  data() {
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      fileList: []
    }
  },
  methods: {
    submitUpload() {
      this.$refs.upload.submit()
    },
    handlePreview(file) {
      console.log(file)
    },
    handleRemove(file) {
      console.log(file)
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    handleDownload(file) {
      console.log(file.raw)
    },
    uploadFile() {
      console.log('111')
      const fd = new FormData()
      for (var i = 0; i < this.$refs.upload.uploadFiles.length; i++) {
        console.log('222')
        fd.set('filename', this.$refs.upload.uploadFiles[i])
        this.$http.post('http://localhost:8080/uploadPics', fd, { emulateJSON: true }
        )
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.user-info{
  margin-top: 50px;
}

</style>
