package com.vvts.projection;

/**
 * @auther kul.paudel
 * @created at 2023-05-17
 */
public interface UserBasicProjection {

    Integer getUserId();

    String getAddress();

    String getEmail();

    String getContact();

    String getName();

    String getCitizenshipNo();

    String getProfilePictureUrl();

    String getCitizenshipFontUrl();

    String getCitizenshipBackUrl();

    Boolean getIsKycPending();

    Boolean getIsKycRejected();

    Boolean getIsKycCompleted();


}
