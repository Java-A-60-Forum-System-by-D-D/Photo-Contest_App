document.addEventListener('DOMContentLoaded', function() {
    const notificationIcon = document.getElementById('notification-icon');
    const notificationDropdown = document.getElementById('notification-dropdown');

    notificationIcon.addEventListener('click', function(event) {
        event.preventDefault();
        notificationDropdown.classList.toggle('show');
    });

  updateNotificationIcon();
});

function updateNotificationIcon() {
    const notificationIcon = document.getElementById('notification-icon');
    const notifications = document.querySelectorAll('.notification-dropdown ul li');
    if (notifications.length === 0) {
        notificationIcon.classList.remove('has-notifications');
    }
}
