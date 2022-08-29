*** Settings ***
Documentation  API Testing in Robot Framework
Library  SeleniumLibrary
Library  RequestsLibrary
Library  JSONLibrary
Library  Collections
Library  OperatingSystem

*** Variables ***
${username}  admin
${password}  admin

${host}  http://localhost:2022

${authorId}  230
${getAuthor}  /authors/${authorId}
${authentication}  /authentication

${authorName}  ZEZO
${authorUsername}  ZERO
${authorPassword}  ader0xc
${authorRole}  1
${createAuthor}  /authors/create/author/
${deleteAuthor}  /authors/${authorId}/
${updateAuthor}  /authors/${authorId}/

${authorTemp}

${token}
${bearer}

*** Test Cases ***
GetBearerTokenTest
    GetBearerTokenBadCredentialsTest
    GetBearerTokenBadCredentialsTest_NoPassword
    GetBearerTokenBadCredentialsTest_NoUsername
    GetBearerTokenBadCredentialsTest_NoBoody
    GetBearerTokenTest_Success

AddAuthorTest
    AddAuthorTest_BAD_CREDENTIAL
    AddAuthorTest_withNoBook_Success
    #AddAuthorTest_author_USERNAME_CONFLICT
    AddAuthorTest_ConflictAuthor
    DeleteAuthorTest_byID_Success
    AddAuthorTest_withBook_Success
    GetAuthorTest_byID_Success
    AddAuthorTest_DisabledAccount
    AddAuthorTest_UNAUTHORIZED_Account
    DeleteAuthorTest_byID_Success
    AddAuthorTest_author_Miss_fields
    AddAuthorTest_author_ASSIGN_ID_ERROR
    AddAuthorTest_author_USERNAME_LENGTH_Problem
    AddAuthorTest_author_PASSWORD_LENGTH_Problem
    AddAuthorTest_AUTHOR_ROLE_BAD_REQUEST

*** Keywords ***
GetBearerTokenBadCredentialsTest
    create session  myssesion  ${host}
    ${body}=  create dictionary  username=${username}  password=aaaaaaaaaaaaaaa
    ${headers}=  create dictionary  Content-Type=application/json
    ${response}=  post request  myssesion  ${authentication}  data=${body}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  403

GetBearerTokenBadCredentialsTest_NoPassword
    create session  myssesion  ${host}
    ${body}=  create dictionary  username=${username}
    ${headers}=  create dictionary  Content-Type=application/json
    ${response}=  post request  myssesion  ${authentication}  data=${body}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  403

GetBearerTokenBadCredentialsTest_NoUsername
    create session  myssesion  ${host}
    ${body}=  create dictionary  password=${password}
    ${headers}=  create dictionary  Content-Type=application/json
    ${response}=  post request  myssesion  ${authentication}  data=${body}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  403

GetBearerTokenBadCredentialsTest_NoBoody
    create session  myssesion  ${host}
    ${body}=  create dictionary
    ${headers}=  create dictionary  Content-Type=application/json
    ${response}=  post request  myssesion  ${authentication}  data=${body}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  403

 GetBearerTokenTest_Success
    create session  myssesion  ${host}
    ${body}=  create dictionary  username=${username}  password=${password}
    ${headers}=  create dictionary  Content-Type=application/json
    ${response}=  post request  myssesion  ${authentication}  data=${body}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  202
    ${jwt}=  Set Variable  ${response.json()}
    ${token}=  Set Variable  ${jwt['jwt']}
    Set Global Variable  ${token}
    Log To Console  ${token}

AddAuthorTest_BAD_CREDENTIAL
    ${bearer}=  Set Variable  Bearer fwdf9687fesd967ff7f74f7496fwsfs96
    Set Global Variable  ${bearer}
    create session  myssesion  ${host}
    ${headers}=  create dictionary  Authorization=${bearer}  Content-Type=application/json
    ${body}=  create dictionary  role=${authorRole}  username=${authorUsername}  password=${authorPassword}  name=${authorName}
    ${response}=  post request  myssesion  ${createAuthor}  data=${body}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  403

AddAuthorTest_withNoBook_Success
    ${bearer}=  Set Variable  Bearer ${token}
    Set Global Variable  ${bearer}
    create session  myssesion  ${host}
    ${headers}=  create dictionary  Authorization=${bearer}  Content-Type=application/json
    ${body}=  create dictionary  role=${authorRole}  username=${authorUsername}  password=${authorPassword}  name=${authorName}
    ${response}=  post request  myssesion  ${createAuthor}  data=${body}  headers=${headers}
    #log to console  ${response.content}
    ${authorDTO}=  Set Variable  ${response.json()}
    ${authorId}=  Set Variable  ${authorDTO['id']}
    Set Global Variable  ${authorId}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  201

AddAuthorTest_ConflictAuthor
    ${bearer}=  Set Variable  Bearer ${token}
    Set Global Variable  ${bearer}
    create session  myssesion  ${host}
    ${headers}=  create dictionary  Authorization=${bearer}  Content-Type=application/json
    ${body}=  create dictionary  role=${authorRole}  username=${authorUsername}  password=${authorPassword}  name=${authorName}
    ${response}=  post request  myssesion  ${createAuthor}  data=${body}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  409

AddAuthorTest_DisabledAccount
    ${username}=  Get From Dictionary  ${authorTemp}  username
    Set Global Variable  ${username}
    #${password}=  Get From Dictionary  ${authorTemp}  password
    ${password}=  Set Variable  ${authorPassword}
    Set Global Variable  ${password}
    GetBearerTokenTest_Success
    ${tokenT}=  Set Variable  ${token}

    ${username}=  Set Variable  admin
    ${password}=  Set Variable  admin
    Set Global Variable  ${username}
    Set Global Variable  ${password}
    GetBearerTokenTest_Success
    DisableAuthorTest_byID_Success

    ${bearer}=  Set Variable  Bearer ${tokenT}
    Set Global Variable  ${bearer}
    create session  myssesion  ${host}
    ${headers}=  create dictionary  Authorization=${bearer}  Content-Type=application/json
    ${body}=  create dictionary  role=${authorRole}  username=${authorUsername}  password=${authorPassword}  name=${authorName}
    ${response}=  post request  myssesion  ${createAuthor}  data=${body}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  400
    EnableAuthorTest_byID_Success

AddAuthorTest_UNAUTHORIZED_Account
    ${username}=  Get From Dictionary  ${authorTemp}  username
    Set Global Variable  ${username}
    #${password}=  Get From Dictionary  ${authorTemp}  password
    ${password}=  Set Variable  ${authorPassword}
    Set Global Variable  ${password}
    GetBearerTokenTest_Success
    ${tokenT}=  Set Variable  ${token}

    ${username}=  Set Variable  admin
    ${password}=  Set Variable  admin
    Set Global Variable  ${username}
    Set Global Variable  ${password}
    GetBearerTokenTest_Success

    ${bearer}=  Set Variable  Bearer ${tokenT}
    Set Global Variable  ${bearer}
    create session  myssesion  ${host}
    ${headers}=  create dictionary  Authorization=${bearer}  Content-Type=application/json
    ${body}=  create dictionary  role=${authorRole}  username=${authorUsername}  password=${authorPassword}  name=${authorName}
    ${response}=  post request  myssesion  ${createAuthor}  data=${body}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  400

AddAuthorTest_withBook_Success

    ${book}  create dictionary  isbn=78999  name=pppp
    ${books}  create list  ${book}

    ${bearer}=  Set Variable  Bearer ${token}
    Set Global Variable  ${bearer}
    create session  myssesion  ${host}
    ${headers}=  create dictionary  Authorization=${bearer}  Content-Type=application/json
    ${body}=  create dictionary  role=${authorRole}  username=${authorUsername}  password=${authorPassword}  name=${authorName}  books=${books}
    ${response}=  post request  myssesion  ${createAuthor}  data=${body}  headers=${headers}
    #log to console  ${response.content}
    ${authorDTO}=  Set Variable  ${response.json()}
    ${authorId}=  Set Variable  ${authorDTO['id']}
    Set Global Variable  ${authorId}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  201

DeleteAuthorTest_byID_Success
    ${bearer}=  Set Variable  Bearer ${token}
    Set Global Variable  ${bearer}
    ${deleteAuthor}=  Set Variable  /authors/${authorId}/
    ${headers}=  create dictionary  Authorization=${bearer}
    ${response}=  delete request  myssesion  ${deleteAuthor}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    log to console  ${response.content}
    should be equal  ${code}  204

DisableAuthorTest_byID_Success
    ${updateAuthor}=  Set Variable  /authors/${authorId}/
    ${bearer}=  Set Variable  Bearer ${token}
    Set Global Variable  ${bearer}
    ${headers}=  create dictionary  Authorization=${bearer}  Content-Type=application/json
    ${authorTemp}=  Set To Dictionary  ${authorTemp}  enabled=${False}
    Pop From Dictionary  ${authorTemp}  books
    ${response}=  put request  myssesion  ${updateAuthor}  data=${authorTemp}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    log to console  ${response.content}
    should be equal  ${code}  200

EnableAuthorTest_byID_Success
    ${updateAuthor}=  Set Variable  /authors/${authorId}/
    ${bearer}=  Set Variable  Bearer ${token}
    Set Global Variable  ${bearer}
    ${headers}=  create dictionary  Authorization=${bearer}  Content-Type=application/json
    ${authorTemp}=  Set To Dictionary  ${authorTemp}  enabled=${True}
    #Pop From Dictionary  ${authorTemp}  books
    ${response}=  put request  myssesion  ${updateAuthor}  data=${authorTemp}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    log to console  ${response.content}
    should be equal  ${code}  200

AddAuthorTest_author_Miss_fields
    ${bearer}=  Set Variable  Bearer ${token}
    Set Global Variable  ${bearer}
    create session  myssesion  ${host}
    ${headers}=  create dictionary  Authorization=${bearer}  Content-Type=application/json
    ${body}=  create dictionary  role=${authorRole}  password=${authorPassword}  name=${authorName}
    ${response}=  post request  myssesion  ${createAuthor}  data=${body}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  400

AddAuthorTest_author_ASSIGN_ID_ERROR
    ${bearer}=  Set Variable  Bearer ${token}
    Set Global Variable  ${bearer}
    create session  myssesion  ${host}
    ${headers}=  create dictionary  Authorization=${bearer}  Content-Type=application/json
    ${body}=  create dictionary  role=${authorRole}  username=${authorUsername}  password=${authorPassword}  name=${authorName}  id=10001
    ${response}=  post request  myssesion  ${createAuthor}  data=${body}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  400

AddAuthorTest_author_PASSWORD_LENGTH_Problem
    ${bearer}=  Set Variable  Bearer ${token}
    Set Global Variable  ${bearer}
    create session  myssesion  ${host}
    ${headers}=  create dictionary  Authorization=${bearer}  Content-Type=application/json
    ${body}=  create dictionary  role=${authorRole}  username=${authorUsername}   password=55  name=${authorName}
    ${response}=  post request  myssesion  ${createAuthor}  data=${body}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  400

AddAuthorTest_author_USERNAME_LENGTH_Problem
    ${bearer}=  Set Variable  Bearer ${token}
    Set Global Variable  ${bearer}
    create session  myssesion  ${host}
    ${headers}=  create dictionary  Authorization=${bearer}  Content-Type=application/json
    ${body}=  create dictionary  role=${authorRole}  username=55   password=${authorPassword}  name=${authorName}
    ${response}=  post request  myssesion  ${createAuthor}  data=${body}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  400

AddAuthorTest_AUTHOR_ROLE_BAD_REQUEST
    ${bearer}=  Set Variable  Bearer ${token}
    Set Global Variable  ${bearer}
    create session  myssesion  ${host}
    ${headers}=  create dictionary  Authorization=${bearer}  Content-Type=application/json
    ${body}=  create dictionary  role=2  username=${authorUsername}I   password=${authorPassword}  name=${authorName}
    ${response}=  post request  myssesion  ${createAuthor}  data=${body}  headers=${headers}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  400

GetAuthorTest_byID_Success
    ${getAuthor}=  set Variable  /authors/${authorId}/
    ${headers}=  create dictionary  Authorization=${bearer}  Content-Type=application/json
    ${response}=  get request  myssesion  ${getAuthor}  headers=${headers}
    ${authorTemp}=  Set Variable  ${response.json()}
    Set Global Variable  ${authorTemp}
    ${code}=  convert to string  ${response.status_code}
    should be equal  ${code}  200