package com.boczj.CommonService.Service;

import com.gz.dubbo.commonServiceException;

public interface RlmsInstallmentService {
    String carInstallmentApplyA(String var1) throws commonServiceException;

    String carInstallmentApplyB(String var1) throws commonServiceException;

    String carDeriveInstallmentApply(String var1) throws commonServiceException;

    String houseDecApply(String var1) throws commonServiceException;

    String maintainIcmsDataMaintain(String var1) throws commonServiceException;

    String queryIcmsData(String var1) throws commonServiceException;

    String querySerialSchedule(String var1, String var2, String var3) throws commonServiceException;

    String querySerialList(String var1) throws commonServiceException;

    String querySerialData(String var1, String var2, String var3) throws commonServiceException;

    String preAuthorLimitCalcul(String var1) throws commonServiceException;

    String preInfoUpdate(String var1) throws commonServiceException;
}
