$(document).ready(function () {
  const path = window.location.pathname;

  $(".nav-item").removeClass("active"); // 모든 nav-item에서 active 클래스 제거

  if (path === '/home') {
    $(".nav-item[data-page='home']").addClass("active"); // MyCard에 active 클래스 추가
  } else if (path === '/wallet-list') {
    $(".nav-item[data-page='wallet']").addClass("active"); // Wallet에 active 클래스 추가
  } else if (path === '/chats') {
    $(".nav-item[data-page='chats']").addClass("active"); // Chats에 active 클래스 추가
  } else if (path === '/mypage') {
    $(".nav-item[data-page='mypage']").addClass("active"); // MyPage에 active 클래스 추가
  }
});
