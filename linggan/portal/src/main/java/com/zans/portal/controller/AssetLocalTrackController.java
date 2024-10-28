package com.zans.portal.controller;

import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.portal.model.AssetLocalTrack;
import com.zans.portal.service.IAssetGuardLineService;
import com.zans.portal.service.IAssetLocalTrackService;
import com.zans.portal.util.HttpHelper;
import com.zans.portal.vo.asset.guardline.req.AssetGuardLineReqVO;
import com.zans.portal.vo.asset.guardline.resp.AssetGuardLineRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * 资产轨迹表(AssetLocalTrack)表控制层
 *
 * @author beixing
 * @since 2022-06-10 17:59:57
 */
@RestController
@RequestMapping("assetLocalTrack")
public class AssetLocalTrackController extends BasePortalController{
    /**
     * 服务对象
     */
    @Autowired
    private IAssetLocalTrackService assetLocalTrackService;

    @Autowired
    IAssetGuardLineService assetGuardLineService;

    @Autowired
    private HttpHelper httpHelper;

    /**
     * 新增一条数据
     *
     * @param assetLocalTrack 实体类
     * @return Response对象
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ApiResult<AssetLocalTrack> insert(@RequestBody AssetLocalTrack assetLocalTrack, HttpServletRequest httpRequest) {
        int result = assetLocalTrackService.insert(assetLocalTrack);
        if (result > 0) {
            return ApiResult.success();
        }
        return ApiResult.error("新增失败");
    }

    /**
     * 修改一条数据
     *
     * @param assetLocalTrack 实体类
     * @return Response对象
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ApiResult<AssetLocalTrack> update(@RequestBody AssetLocalTrack assetLocalTrack, HttpServletRequest httpRequest) {
        assetLocalTrackService.update(assetLocalTrack);
        return ApiResult.success();
    }

    /**
     * 删除一条数据
     *
     * @param assetLocalTrack 参数对象
     * @return Response对象
     */
    @RequestMapping(value = "del", method = RequestMethod.POST)
    public ApiResult<AssetLocalTrack> delete(@RequestBody AssetLocalTrack assetLocalTrack, HttpServletRequest httpRequest) {
        assetLocalTrackService.deleteById(assetLocalTrack.getId());
        return ApiResult.success();
    }



    @RequestMapping(value = "init", method = {RequestMethod.GET,RequestMethod.POST})
    public ApiResult init() {
        List<AssetGuardLineRespVO> guardLineRespVOs = assetGuardLineService.getListByName(null);
        Map<String, Object> result = MapBuilder.getBuilder()
                .put("group_name", guardLineRespVOs)
                .build();
        return ApiResult.success(result);
    }



    @RequestMapping(value = "getPointList", method = {RequestMethod.POST})
    public ApiResult getPointList(@RequestBody AssetGuardLineReqVO reqVO) {
        super.checkPageParams(reqVO, null);
        return assetGuardLineService.getPointList(reqVO);
    }



    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ApiResult<AssetLocalTrack> list(@RequestBody AssetLocalTrack assetLocalTrack) {

        return assetLocalTrackService.list(assetLocalTrack);
    }

    @RequestMapping(value = "view", method = RequestMethod.POST)
    public ApiResult view(@RequestBody AssetLocalTrack assetLocalTrack) {
        return assetLocalTrackService.view(assetLocalTrack);
    }




}
