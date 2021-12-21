<template>
  <div>
    <el-card shadow="hover" class="mgb20" style="height:600px;">
      <div class="user-info">
        <el-upload
          ref="upload"
          class="upload-demo"
          action="http://localhost:8080/uploadPics"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :file-list="fileList"
          :auto-upload="false"
          :data="{username:multipartFile.username,filename:multipartFile.filefolder}"
        >
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
          <div slot="tip" class="el-upload__tip">单次只能上传一个jpg/png文件</div>
        </el-upload>
      </div>
      <el-container class="PicInfo">
        <el-col :span="2" class="file_tips">
          请输入需要上传的项目目录
        </el-col>
        <el-col :span="5" class="file">
          <el-input v-model="multipartFile.filefolder" placeholder="请输入项目名" :disabled="false" />
        </el-col>
      </el-container>
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
      fileList: [],
      multipartFile: { username: 'test', filefolder: 'default' },
      videoInfo: { username: 'default', filefolder: 'default' }
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
        console.log(this.$refs.upload.uploadFiles[i])
        fd.set('filename', this.$refs.upload.uploadFiles[i].raw)
        this.$http.post('http://localhost:8080/uploadPics', fd)
          .then(function(res) {
            console.log('ok')
          })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.user-info{
  margin-top: 100px;
}
.FileName{
  font-size: 0px;
}
.PicInfo{
  margin-top: 40px;
}
.file{
  margin-left: 30px;
}
.pic_tip{
  margin-bottom: 20px;
}
.file_tips{
  font-size: 15px;
}
</style>
