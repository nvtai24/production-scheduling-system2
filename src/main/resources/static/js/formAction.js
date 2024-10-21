function clear(element) {
    let row = element.closest('tr');

    // Clear values in the inputs
    row.querySelector('.quantity-input').value = '';
    row.querySelector('.effort-input').value = '';
}

function toggleEdit(button) {
    // Tìm hàng hiện tại chứa button Edit
    let row = button.closest('tr');
    let inputs = row.querySelectorAll('.edit-input');
    let displays = row.querySelectorAll('.text-display');
    let formActionInline = row.querySelector('.form-action-inline');
    let deleteButton = row.querySelector('.btn-delete'); // Lấy nút Xóa

    // Kiểm tra xem đã ở chế độ chỉnh sửa hay chưa
    if (!button.classList.contains('editing')) {
        // Bật chế độ chỉnh sửa: hiện inputs và ẩn text
        inputs.forEach(input => input.style.display = 'inline');
        displays.forEach(display => {
            if (!display.classList.contains('name-display')) { // giữ nguyên tên worker
                display.style.display = 'none';
            }
        });

        // Ẩn nút Xóa
        deleteButton.style.display = 'none';

        // Thay đổi nút thành Save và Cancel
        button.innerHTML = '<i class="fa-solid fa-check"></i>';
        button.classList.remove('btn-edit');    // remove color to set new color
        button.classList.add('editing', 'btn-save-add');

        // Tạo nút Cancel
        let cancelButton = document.createElement('button');
        cancelButton.innerHTML = '<i class="fa-solid fa-xmark"></i>';
        cancelButton.classList.add('btn-icon', 'btn-cancel');
        cancelButton.type = 'button';

        // Khi bấm Cancel thì quay lại trạng thái ban đầu
        cancelButton.onclick = function() {
            inputs.forEach(input => input.style.display = 'none');
            displays.forEach(display => display.style.display = 'inline');
            button.innerHTML = '<i class="fa-solid fa-pen"></i>';
            button.classList.remove('editing', 'btn-save-add');
            button.classList.add('btn-edit');
            deleteButton.style.display = 'inline'; // Hiển thị lại nút Xóa
            cancelButton.remove(); // Xóa nút Cancel
        };

        // Thêm nút Cancel vào sau nút Save
        formActionInline.appendChild(cancelButton);
    } else {
        // Gom các trường input từ hàng hiện tại vào form và submit
        let formEdit = document.createElement('form');
        formEdit.action = '/worker/edit';
        formEdit.method = 'post';

        // Di chuyển các input từ row sang formEdit
        inputs.forEach(input => {
            formEdit.appendChild(input);
        });

        // Thêm input chứa eid nếu không bị di chuyển
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
    return confirm("Are you sure you want to delete this one?");
}