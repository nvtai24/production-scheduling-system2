function clearInputs(element) {
    // Find the closest row that contains the button
    let row = element.closest('tr');

    // Find the input fields inside that row
    let quantityInput = row.querySelector('.quantity-input');
    let effortInput = row.querySelector('.effort-input');

    // Clear the input values if they exist
    if (quantityInput) {
        quantityInput.value = '';
    }

    if (effortInput) {
        effortInput.value = '';
    }
}
function toggleEdit(button) {
    // Tìm hàng hiện tại chứa nút Edit
    let row = button.closest('tr');
    let inputs = row.querySelectorAll('.edit-input');
    let displays = row.querySelectorAll('.text-display');
    let deleteButton = row.querySelector('.btn-delete'); // Nút Delete

    // Kiểm tra xem đã ở chế độ chỉnh sửa chưa
    if (!button.classList.contains('editing')) {
        // Bật chế độ chỉnh sửa: hiện các input và ẩn text hiển thị
        inputs.forEach(input => input.classList.remove('hidden'));
        displays.forEach(display => display.classList.add('hidden'));

        // Ẩn nút Delete và đổi nút Edit thành Save
        deleteButton.style.display = 'none';
        button.innerHTML = '<i class="fa-solid fa-check"></i>';
        button.classList.remove('bg-blue-500', 'btn-edit');
        button.classList.add('bg-green-500', 'editing');

        // Thêm nút Cancel
        let cancelButton = document.createElement('button');
        cancelButton.innerHTML = '<i class="fa-solid fa-xmark"></i>';
        cancelButton.classList.add('bg-red-500', 'text-white', 'px-3', 'py-1', 'rounded-md', 'text-xs', 'md:text-sm');
        cancelButton.type = 'button';

        // Khi nhấn Cancel, quay lại trạng thái ban đầu
        cancelButton.onclick = function() {
            inputs.forEach(input => input.classList.add('hidden'));
            displays.forEach(display => display.classList.remove('hidden'));
            button.innerHTML = '<i class="fa-solid fa-pen"></i>';
            button.classList.remove('bg-green-500', 'editing');
            button.classList.add('bg-blue-500', 'btn-edit');
            deleteButton.style.display = 'inline'; // Hiển thị lại nút Delete
            cancelButton.remove(); // Xóa nút Cancel
        };

        // Thêm nút Cancel vào sau nút Save
        row.querySelector('.form-action-inline').appendChild(cancelButton);

    } else {
        // Khi nhấn Save, submit các input để chỉnh sửa
        let formEdit = document.createElement('form');
        formEdit.action = '/worker/edit';
        formEdit.method = 'post';

        // Di chuyển các input từ row sang formEdit để submit
        inputs.forEach(input => formEdit.appendChild(input));

        // Thêm input chứa eid nếu chưa bị di chuyển
        let eidInput = row.querySelector('input[name="eid"]');
        if (eidInput) {
            formEdit.appendChild(eidInput);
        }

        // Thêm form vào body và submit
        document.body.appendChild(formEdit);
        formEdit.submit();
    }
}

function updateHourlyRate(selectElement) {
    const selectedOption = selectElement.options[selectElement.selectedIndex];
    const newHourlyRate = selectedOption.getAttribute('data-salary');
    const row = selectElement.closest('tr');
    const hourlyRateDisplay = row.querySelector('.hourly-rate');
    hourlyRateDisplay.textContent = newHourlyRate + 'K';
}

function confirmDelete() {
    return confirm("Are you sure you want to delete this worker?");
}