$(document).ready(function () {
  // navBar active 조정
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


// Toast 알림
function showToast(message, type, duration) {
  const $toast = $("#toast");

  // 이미 표시 중인 알림이 있을 경우 한 번 닫기
  if ($toast.hasClass("show")) {
    $toast.animate({ opacity: 0, bottom: '10%' }, 500, () => {
      $toast.removeClass("show error success");
      // 알림을 숨긴 후 새 메시지로 다시 showToast 실행
      displayToast(message, type, duration);
    });
  } else {
    displayToast(message, type, duration);
  }
}

function displayToast(message, type, duration) {
  const $toast = $("#toast");
  $toast.text(message);

  // 오류 또는 정상 동작 클래스 추가
  if (type === "error") {
    $toast.removeClass("success").addClass("show error");
  } else if (type === "success") {
    $toast.removeClass("error").addClass("show success");
  } else {
    $toast.addClass("show");
  }

  // 초기 fade-in 애니메이션
  $toast.css({ opacity: 0, bottom: '10%' }).animate({ opacity: 1, bottom: '13%' }, 500);

  // 지정된 시간 후에 알림 숨김 및 fade-out 애니메이션
  setTimeout(() => {
    $toast.animate({ opacity: 0, bottom: '10%' }, 500, () => {
      $toast.removeClass("show error success");
    });
  }, duration);
}

function handleError(message, duration = 3000) {
  showToast(message, "error", duration);
}

function handleSuccess(message, duration = 3000) {
  showToast(message, "success", duration);
}
