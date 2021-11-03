package ext.form;

import com.alibaba.fastjson.JSON;
import com.qygly.demo.ext.form.model.FieldInfoModel;
import com.qygly.demo.ext.form.model.FormCreateModel;
import com.qygly.demo.ext.form.model.FormInfoModel;
import org.assertj.core.util.Lists;
import org.junit.Test;

/**
 * @author xuzhifeng
 * @Date 2021/11/2 2:44 下午
 */
public class ApDocExtTest {

    @Test
    public void testCreate() {
        try {
            String json = JSON.toJSONString(createFormCreateModel());
            System.out.println(json);

            FormCreateModel formCreateModel = JSON.parseObject(json, FormCreateModel.class);
            System.out.println(formCreateModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FormCreateModel createFormCreateModel() {
        FormCreateModel formCreateModel = new FormCreateModel();
        formCreateModel.setFormName("测试");
        formCreateModel.setFormType("1");
        formCreateModel.setFormInstructions("测试");
        formCreateModel.setMasterMask("测试");
        formCreateModel.setFormNo("BDZJ111");

        FormInfoModel formInfoModel = new FormInfoModel();
        formInfoModel.setFormInfoList(Lists.newArrayList(
                new FieldInfoModel("key1", "v1", Boolean.TRUE, 1),
                new FieldInfoModel("key2", "v2", Boolean.TRUE, 2)));
        formInfoModel.setFormInfoFieldCount(2);
        formInfoModel.setFormInfoListCount(4);

        formInfoModel.setCustomFieldList(Lists.newArrayList(
                new FieldInfoModel("key1", "v1", Boolean.TRUE, 1),
                new FieldInfoModel("key2", "v2", Boolean.TRUE, 2)));
        formInfoModel.setCustomFieldCount(2);
        formInfoModel.setCustomFieldListCount(4);

        formCreateModel.setFormInfo(formInfoModel);
        return formCreateModel;
    }
}
