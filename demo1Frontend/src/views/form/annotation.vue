<template>
  <div>
    <el-form :model="formInline">
      <el-form-item label="数据集：">
        <el-select v-model="values" placeholder="选择数据集" @change="selectOne">
          <el-option
            v-for="item in dataList"
            :key="item.code"
            :label="item.name"
            :value="item.code"
          />
        </el-select>
        <el-button type="primary" style="margin-left: 20px" @click="changeDataset">选择</el-button>
      </el-form-item>

      <el-form-item label="图片筛选：">
        <el-radio-group v-model="formInline.radio" @change="handleChange"> <!--这个功能暂不实现-->
          <el-radio-button label="全部" />
          <el-radio-button label="未标注" />
          <el-radio-button label="已标注" />
        </el-radio-group>
      </el-form-item>
      <!-- <el-form-item>
        <el-button type="primary">选择</el-button>
      </el-form-item> -->
    </el-form>

    <!-- 图片导航 -->
    <div class="pics">
      <div class="arrow arrow-left" @click="showMore('down')" />
      <div class="pic-container">
        <div ref="picContainer" class="pic-box">
          <div v-for="(v, i) in pics" :key="i" class="pic">
            <div
              class="info"
              :style="{ 'background-image': 'url(' + v.cropImage + ')' }"
              @click="activePic(v.cropImage,v.code)"
            />
          </div>
        </div>
      </div>
      <div class="arrow arrow-right" @click="showMore('up')" />
    </div>

    <el-row :gutter="10" class="tagList">
      <el-col :span="17">
        <ui-marker
          ref="aiPanel-editor"
          class="ai-observer"
          :unique-key="uuid"
          :ratio="11/ 6"
          :img-url="currentInfo.currentBaseImage"
          @vmarker:onImageLoad="onImageLoad"
        />
      </el-col>
      <el-col :span="6">
        <div class="title">标签</div>
        <div v-for="(v, i) in tags" :key="i" class="tags">
          <el-tag size="small" @click="setTag(v)">
            {{ v.tagName }}
          </el-tag>
          <i class="el-icon-delete" @click="delTag(i)" />
        </div>
        <el-row>
          <el-button type="success" class="handleButton" @click="addTag">
            添加标签
          </el-button>
        </el-row>
        <el-button type="primary" class="handleButton" @click="submitForm">
          提交标注
        </el-button>
      </el-col>
    </el-row>

    <!-- 添加标签 dialog -->
    <el-dialog
      width="30%"
      title="添加标签"
      :visible.sync="innerVisible"
      :before-close="beforeClose"
    >
      <el-form ref="innerForm" :model="innerForm" :rules="tep_rules">
        <el-form-item label="标签名称：" prop="tagName">
          <el-input v-model="innerForm.tagName" />
        </el-form-item>
        <el-form-item label="标签编码：" prop="tag">
          <el-input v-model="innerForm.tag" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="createForm('innerForm')">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
// import { AIMarker } from 'Vue-Picture-BD-Marker'
import { AIMarker } from 'vue-picture-bd-marker'
import axios from 'axios'
import qs from 'qs'

export default {
  name: 'StagePicPage',
  components: { 'ui-marker': AIMarker },
  data() {
    return {
      values: '',
      filename: '',
      publisher: '',
      currentDataList: 0,
      formInline: {
        region: '',
        radio: '全部'
      },
      dataList: [ // 改成所有已领取数据集的名称
        // { name: '火焰', code: 2, publisher: '' }
      ],
      dataset: 0,
      uuid: '0da9130',
      // 当前图片的信息，包含图片原本的高矮胖瘦尺寸
      currentInfo: {
        currentBaseImage: '',
        // 'https://seopic.699pic.com/photo/50041/3365.jpg_wh1200.jpg',默认为第一张
        rawW: 200,
        rawH: 100,
        currentW: 200,
        currentH: 100,
        currentCode: 0,
        checked: false, // false表示当前图片还没有标记过
        data: [] // 表示图片矩形标记信息
      },

      // *****************************
      pics: [], // 默认加载第一个领取的数据集
      filepath: [],
      active: 0, // 当前图片序号
      picTotal: 10, // 照片总数

      // *********************************************
      tags: [
        {
          tagName: '小蜜蜂',
          tag: '0x0001'
        },
        {
          tagName: '汽车',
          tag: '0x0002'
        }
      ],
      allInfo: [], // 图片的矩形标记信息集合
      imageInfo: [], // 存储图片原始信息

      innerVisible: false,
      innerForm: {
        tagName: '',
        tag: ''
      },

      tep_rules: {
        tagName: [{ required: true, message: '请输入', trigger: 'blur' }],
        tag: [{ required: true, message: '请输入', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    // this.onImageLoad()
    this.Init()
  },
  methods: {
    /** 记录图片当前的大小和原始大小 data={rawW,rawH,currentW,currentH} */
    Init() {
      var _this = this
      var user = localStorage.getItem('username')
      axios.get('http://localhost:8080/getAllRnS', {
        params: {
          username: user
        }
      })
        .then(function(reponse) {
          var data = reponse.data
          console.log(reponse)
          for (var i = 0; i < data.length; i++) {
            var t = data[i].picfilepath
            // console.log(t)
            _this.filepath.push(t)
            const temp = { name: data[i].filename, code: i + 1, publisher: data[i].publisher }
            _this.dataList.push(temp)
          }
          var filename = _this.dataList[0].name
          var publisher = _this.dataList[0].publisher
          console.log(_this.dataList[0], 'oo')
          console.log(filename)
          console.log(publisher)
          localStorage.setItem('dataListName', filename)
          localStorage.setItem('dataListPublisher', publisher)
          // 这里复现一下第一步
          axios.get('http://localhost:8080/getFilePicsData', {
            params: {
              username: publisher,
              filename: filename
            }
          })
            .then(function(reponse) {
              var data = reponse.data
              console.log('have data', data)
              // this.imageInfo = data
              // console.log(this.imageInfo)
              for (var i = 0; i < data.length; i++) { // 在这个地方传入mark的 信息
                var tt = data[i].isMarked
                console.log('is mark is', tt)
                var temp = { picname: data[i].filename, str: data[i].base64str, width: 200, height: 100,
                  isMark: data[i].isMarked,code: i }
                console.log('temp is', temp)
                _this.imageInfo.push(temp)
              }
              // console.log(_this.imageInfo) //默认set所有图片
              for (var j = 0; j < _this.imageInfo.length; j++) {
                var m = _this.imageInfo[j].str
                var x= _this.imageInfo[j].code
                const t = { cropImage: m,code: x}
                _this.pics.push(t)
              }
              console.log(_this.imageInfo, 'imageinfo')

              // 修改 currentinfo //其他信息先不传入
              _this.currentInfo.currentBaseImage = _this.pics[0].cropImage
              _this.currentInfo.code = _this.pics[0].code
            })
        })
    },
    changeDataset() {
      //
      var _this = this
      // eslint-disable-next-line no-unused-vars
      var user, file
      var t = _this.currentDataList
      for (var i = 0; i < _this.dataList.length; i++) {
        var m = _this.dataList[i].code
        var x = _this.dataList[i].name
        console.log('m is', m)
        console.log(x)
        console.log('t is', t)
        if (m === t) {
          console.log('matches code at ', i)
          user = _this.dataList[i].publisher
          file = _this.dataList[i].name
          break
        }
      }
      localStorage.setItem('dataListName', file)
      localStorage.setItem('dataListPublisher', user)
      //
      // 获取当前数据集的所有图片信息
      console.log('username is', user)
      console.log('filename is', file)
      axios.get('http://localhost:8080/getFilePicsData', {
        params: {
          username: user,
          filename: file
        }
      })
        .then(function(reponse) {
          var data = reponse.data
          console.log('have mark', data)
          _this.imageInfo = []// 清空一下
          // console.log(this.imageInfo)
          for (var i = 0; i < data.length; i++) { // 这个地方也有修改
            var temp = { picname: data[i].filename, str: data[i].base64str, isMark: data[i].mark }
            _this.imageInfo.push(temp)
          }
          // console.log(_this.imageInfo)
          _this.pics = [] // 也清空一下
          for (var j = 0; j < _this.imageInfo.length; j++) {
            var m = _this.imageInfo[j].str
            var x= _this.imageInfo[j].code
            const t = { cropImage: m ,code:x}
            _this.pics.push(t)
          }
          // 修改 currentinfo //其他信息先不传入
          _this.currentInfo.currentBaseImage = _this.pics[0].cropImage
          _this.currentInfo.code=_this.pics[0].code
        })
      //
    },
    notMark() {
      var _this = this
      console.log(_this.imageInfo, 'check image info')
      _this.pics = [] // 清空一下
      for (var j = 0; j < _this.imageInfo.length; j++) {
        var temp = _this.imageInfo[j].isMark
        console.log(temp, 'iiiii')
        if (temp === false) {
          console.log(j)
          var m = _this.imageInfo[j].str
          var x= _this.imageInfo[j].code
          const t = { cropImage: m , code:x}
          _this.pics.push(t)
        }
      }
      // 修改 currentinfo //其他信息先不传入
      if (_this.pics.length > 0) {
        _this.currentInfo.currentBaseImage = _this.pics[0].cropImage
        _this.currentInfo.code=_this.pics[0].code
      } else {
        _this.currentInfo.currentBaseImage = '';
      }
    },
    marked() {
      var _this = this
      _this.pics = [] // 清空一下
      for (var j = 0; j < _this.imageInfo.length; j++) {
        console.log('in loop', j)
        if (_this.imageInfo[j].isMark === true) {
          var m = _this.imageInfo[j].str
          var x = _this.imageInfo[j].code
          const t = { cropImage: m , code: x}
          _this.pics.push(t)
        }
      }
      // 修改 currentinfo //其他信息先不传入
      if (_this.pics.length > 0) {
        console.log('go this')
        _this.currentInfo.currentBaseImage = _this.pics[0].cropImage
        _this.currentInfo.code=_this.pics[0].code
      } else {
        console.log('go that')
        _this.currentInfo.currentBaseImage = ''
      }
    },
    all() {
      var _this = this
      _this.pics = [] // 清空一下
      for (var j = 0; j < _this.imageInfo.length; j++) {
        var m = _this.imageInfo[j].str
        var x= _this.imageInfo[j].code
        const t = { cropImage: m , code:x}
        _this.pics.push(t)
      }
      // 修改 currentinfo //其他信息先不传入
      _this.currentInfo.currentBaseImage = _this.pics[0].cropImage
    },
    selectOne(value) {
      console.log('ttttt', value)
      // 存储当前的选择
      this.currentDataList = value
    },
    onImageLoad(data) {
      console.log(data)
      this.imageInfo = data
    },

    setTag(v) {
      this.$refs['aiPanel-editor'].getMarker().setTag(v)
    },
    addTag() {
      this.innerVisible = true
      this.innerForm.tagName = ''
      this.innerForm.tag = ''
    },
    delTag(index) {
      this.tags.splice(annotation, 1)
    },
    close() {
      this.innerVisible = false
      this.$refs['innerForm'].resetFields()
    },
    beforeClose(done) {
      this.$refs['innerForm'].resetFields()
      done()
    },
    createForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          for (const index in this.tags) {
            const item = this.tags[index]
            if (
              item.tagName === this.innerForm.tagName ||
              item.tag === this.innerForm.tag
            ) {
              this.$message.warning('标签名或标签值已存在，请重新输入')
              return
            }
          }
          this.tags.push({
            tagName: this.innerForm.tagName,
            tag: this.innerForm.tag
          })
          this.innerVisible = false
        }
      })
    },
    /**
     * 完成标记，提交标记集合
     */
    submitForm() {
      // 要把该图片的mark置掉
      var _this=this
      const data = this.$refs['aiPanel-editor'].getMarker().getData()
      this.allInfo = data
      console.log(this.allInfo)
      console.log(this.currentInfo, 'currentInfo')
      const size = {
        width: this.currentInfo.rawW, //
        height: this.currentInfo.rawH
      }
      console.log('size is', size)
      const Labels = {
        filename: '',
        username: '',
        picname: '',
        height: 0,
        width: 0,
        Xmin: [],
        Xmax: [],
        Ymin: [],
        Ymax: [],
        tagName: [],
        tag: []
      }
      for (let i = 0; i < this.allInfo.length; i++) {
        console.log(i)
        const xmin =
          ((parseInt(this.allInfo[i].position.x
            .substring(0, this.allInfo[i].position.x.length - 1)) * size.width) / 100)
            .toFixed(0).toString()
        console.log(xmin, '左上')
        const ymin =
          ((parseInt(this.allInfo[i].position.y
            .substring(0, this.allInfo[i].position.y.length - 1)) * size.height) / 100)
            .toFixed(0).toString()
        console.log(ymin, '右手上')
        const xmax =
          ((parseInt(this.allInfo[i].position.x1
            .substring(0, this.allInfo[i].position.x1.length - 1)) * size.width) / 100)
            .toFixed(0).toString()
        console.log(xmax, '右上')
        const ymax =
          ((parseInt(this.allInfo[i].position.y1
            .substring(0, this.allInfo[i].position.y1.length - 1)) * size.height) / 100)
            .toFixed(0).toString()
        console.log(ymax, '左上')
        console.log('here1')
        const tagName = this.allInfo[i].tagName
        console.log('here2')
        const tag = this.allInfo[i].tag
        console.log(tagName)
        console.log('here3')
        Labels.Xmin.push(xmin)
        Labels.Xmax.push(xmax)
        Labels.Ymin.push(ymin)
        Labels.Ymax.push(ymax)
        Labels.tagName.push(tagName)
        Labels.tag.push(tag)
      }
      var a = localStorage.getItem('dataListName')
      console.log(a,'a')
      console.log(b,'b')
      var b = localStorage.getItem('dataListPublisher')
      var s=_this.currentInfo.code
      console.log('curr coe',s)
      Labels.width = size.width
      Labels.height = size.height
      Labels.filename = a
      Labels.picname = _this.imageInfo[s].picname// 注意修改
      Labels.username = b
      const obj = {
        username: Labels.username,
        filename: Labels.filename,
        picname: Labels.picname,
        height: Labels.height,
        width: Labels.width,
        xmin: Labels.Xmin,
        xmax: Labels.Xmax,
        ymin: Labels.Ymin,
        ymax: Labels.Ymax,
        tagName: Labels.tagName,
        tag: Labels.tag
      }
      qs.stringify(obj)
      this.$http.post('http://localhost:8080/annotation', { // 检查是否修改mark变量
        username: Labels.username,
        filename: Labels.filename,
        picname: Labels.picname,
        height: Labels.height,
        width: Labels.width,
        xmin: Labels.Xmin,
        xmax: Labels.Xmax,
        ymin: Labels.Ymin,
        ymax: Labels.Ymax,
        tagName: Labels.tagName,
        tag: Labels.tag
      }, { emulateJSON: true })
        .then(function(response) {
          console.log(response.data)
          _this.reload()
        })
        // eslint-disable-next-line handle-callback-err
        .catch(function(error) {
          this.loading = false
        })
    },

    // 点击左右按钮显示更多
    showMore(v) {
      const el = this.$refs.picContainer

      if (v === 'up') {
        this.active++
        if (this.active >= this.picTotal - 3) {
          // 最后4张图
          this.active = this.pics.length - 3
          return
        }
        if (
          this.pics.length - 3 === this.active &&
          this.pics.length < this.picTotal
        ) {
          this.photoPageIndex++
          this.getPhotos()
          return
        }
      } else {
        this.active--
        if (this.active < 0) this.active = 0
      }
      el.style.transform =
        'translateX(-' + (this.active / this.pics.length) * 100 + '%)'
    },

    getPhotos() {
      return this.$nextTick(() => {
        const el = this.$refs.picContainer
        if (el) {
          el.style.width = el.scrollWidth + 'px'

          el.style.transform =
            'translateX(-' + (this.active / this.pics.length) * 100 + '%)'
        }
      })
    },
    /** 得到当前点击图片*/
    activePic(v,t) {
      console.log('v',t)
      this.currentInfo.currentBaseImage = v
      this.currentInfo.code=t
    },

    handleChange(label) {
      console.log(label, 'label is')
      if (label === '全部') {
        console.log('1')
        this.all()
      } else if (label === '已标注') {
        console.log('3')
        this.marked()
      } else {
        console.log('2')
        this.notMark()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.pics {
  width: 100%;
  overflow: hidden;
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
  align-items: center;

  .arrow {
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background-image: url('../../assets/404_images/404.png');
    background-repeat: no-repeat;
    background-size: contain;

    &.arrow-right {
      transform: rotate(180deg);
    }
  }

  .pic-container {
    // width: 1180px;
    width: calc(100% - 30px);
    height: 104px;
    margin: 0px auto;
    overflow: hidden;

    .pic-box {
      height: 100%;
      // min-width: 1180px;
      min-width: calc(100% - 50px);
      transition: all 0.5s linear;
      display: flex;
      flex-wrap: nowrap;
    }

    .pic {
      float: left;
      border: 1px solid #ccc;
      box-sizing: border-box;
      margin-right: 10px;
      margin-left: 10px;
      width: 185px;
      height: 114px;

      .info {
        width: 183px;
        height: 100%;
        background-size: 100%;
        background-repeat: no-repeat;
        background-position: center;
        position: relative;

        &:hover {
          border: 1px solid skyblue;
        }
      }
    }
  }
}

.tagList {
  padding-left: 10px;
  padding-bottom: 30px;

  .title {
    text-align: center;
    font-weight: bold;
  }

  .handleButton {
    width: 100%;
    margin-bottom: 10px;
  }

  .tags {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;

    .el-icon-delete {
      cursor: pointer;
    }
  }
}
</style>

