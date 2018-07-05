<style >

.ivu-form-item {
    margin-bottom: 1px;
    vertical-align: top;
    /*zoom: 1;*/
}

</style>
<template>

      <div v-if="result">

          <Tabs type="card">
             <TabPane label="Basic" icon="code-working">


               <Form class='settings-form-item'>
                 <Form-item  label="StartTime:" >
                    <span v-text="result.startTime"></span>
                 </Form-item>
               </Form>
               <Form style="" v-if="result.endTime">
                 <Form-item  label="EndTime:" >
                    <span v-text="result.endTime"></span>
                 </Form-item>
               </Form>
               <Form style="" v-if="result.duration">
                 <Form-item  label="Duration:" >
                    <span v-text="result.duration + ' ms'"></span>
                 </Form-item>
               </Form>
               <!-- <Form style=" margin-bottom: 5px;">
                 <Form-item  label="Return value:" > -->
                     <json-editor ref="resultEditor"  :json="result.testcaseResult" style="height:600px;" />
                 <!-- </Form-item>
               </Form> -->



             </TabPane>



             <TabPane label="Checkpoint" icon="android-checkmark-circle">

               <Row type="flex" >
                 <Col offset="1">
                   <Timeline>
                         <Timeline-item :color="getCheckpointStatusColor(checkpoint)" v-for="(checkpoint, index) in result.checkpointList" :key="index">
                           <span  v-text="checkpoint.timeStamp"> </span>
                           <span style="font-size: 15px;" v-text="checkpoint.name + ':'"> </span>

                           <!-- <div v-if="checkpoint.ruleDescription">
                             <span style="font-weight:bold;"> Rule: </span> <span  v-text="checkpoint.ruleDescription"> </span>
                           </div> -->
                           <!-- <div v-if="checkpoint.jsonPath">
                             <span style="font-weight:bold;"> JsonPath: </span> <span  v-text="checkpoint.jsonPath"> </span>
                           </div> -->
                           <!-- <div v-if="checkpoint.expectedValue">
                             <span style="font-weight:bold;"> Expected: </span> <span  v-text="checkpoint.expectedValue"> </span>
                           </div> -->
                           <div v-if="checkpoint.actualValue">
                             <span style="font-weight:bold;"> Actual: </span> <span  v-text="checkpoint.actualValue"> </span>
                           </div>
                           <div v-if="checkpoint.errorMessage">
                             <span style="font-weight:bold;"> Error: </span> <span  v-text="checkpoint.errorMessage"> </span>
                           </div>

                           <div v-if="checkpoint.screenshotUrl">
                             <span style="font-weight:bold;"> Screenshot: </span> <a :href="checkpoint.screenshotUrl" target="_blank"> display</a> </span>
                           </div>


                         </Timeline-item>
                   </Timeline>
                 </Col>
               </Row>
             </TabPane>


            <TabPane label="Log" icon="ios-list-outline">
              <Row type="flex" >
                <Col offset="1">
                  <Timeline>
                        <Timeline-item v-for="(logItem, index) in result.logItemList" :key="index">
                          <span  v-text="logItem.timestamp"> </span>
                          <span  v-text="logItem.content"> </span>
                        </Timeline-item>
                  </Timeline>
                </Col>
              </Row>
            </TabPane>
         </Tabs>
    </div>
</template>
<script>
  import {mapGetters, mapActions} from 'vuex'
  import JSONEditor from '../../components/control/JsonEditor'
  // import utils from '../../lib/utils'
  export default {
    props: ['result'],
    data () {
      return {
        defaultPanel: '1'
      }
    },
    components: {
      'json-editor': JSONEditor
    },
    created: function () {

    },
    mounted: function () {
      this.updateResultToJsonEditor()
    },
    watch: {
      'result': function (val) {
        this.updateResultToJsonEditor()
      }
    },
    computed: {
      ...mapGetters([
      ])
    },
    methods: {
      ...mapActions([]),
      // getformattedTimeStamp: function (checkpoint) {
      //   return utils.formatDateFromMilliseconds(checkpoint.timeStamp, 'HH:mm:ss')
      // },
      getCheckpointStatusColor: function (checkpoint) {
        if (!checkpoint.evaluated) {
          return 'grey'
        }
        return checkpoint.passed === true ? 'green' : 'red'
      },
      updateResultToJsonEditor: function () {
        if (this.result.testcaseResult) {
          this.$refs.resultEditor.editor.set(this.result.testcaseResult)
        } else {
          this.$refs.resultEditor.editor.set({
            errMessage: this.result.errMessage,
            stackTrace: this.result.stackTrace
          })
        }
      }
    }
  }
</script>
