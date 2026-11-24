document.addEventListener('DOMContentLoaded', () => {
    // 1. Modales / Drawers
    document.querySelectorAll('[data-open-modal]').forEach(btn => {
        btn.addEventListener('click', () => {
            const target = document.getElementById(btn.dataset.openModal);
            if (target) target.classList.add('open');
        });
    });

    document.querySelectorAll('[data-close-modal]').forEach(btn => {
        btn.addEventListener('click', () => {
            btn.closest('.ht-modal-backdrop')?.classList.remove('open');
        });
    });

    document.querySelectorAll('.ht-modal-backdrop').forEach(backdrop => {
        backdrop.addEventListener('click', (e) => {
            if (e.target === backdrop) backdrop.classList.remove('open');
        });
    });

    // 2. Cotizador Dinámico de Reserva
    const roomSelect = document.getElementById('htRoomSelect');
    const checkInInput = document.getElementById('htCheckIn');
    const checkOutInput = document.getElementById('htCheckOut');
    const quoteNights = document.getElementById('htQuoteNights');
    const quoteTotal = document.getElementById('htQuoteTotal');

    function updateStayEstimate() {
        if (!roomSelect || !checkInInput || !checkOutInput || !quoteNights || !quoteTotal) return;
        const opt = roomSelect.options[roomSelect.selectedIndex];
        const price = parseFloat(opt?.dataset?.price || '0');
        const inD = checkInInput.value ? new Date(checkInInput.value + 'T00:00:00') : null;
        const outD = checkOutInput.value ? new Date(checkOutInput.value + 'T00:00:00') : null;

        if (inD && outD && outD > inD && price > 0) {
            const nights = Math.round((outD - inD) / 86400000);
            quoteNights.textContent = `${nights} ${nights === 1 ? 'noche' : 'noches'} × $${price.toLocaleString('es-CO')}`;
            quoteTotal.textContent = `$${(nights * price).toLocaleString('es-CO')}`;
        } else if (inD && outD && outD <= inD) {
            quoteNights.textContent = 'La fecha de salida debe ser posterior a la entrada';
            quoteTotal.textContent = '—';
        } else {
            quoteNights.textContent = 'Elige suite y fechas para calcular el total';
            quoteTotal.textContent = '$0';
        }
    }

    if (roomSelect && checkInInput && checkOutInput) {
        roomSelect.addEventListener('change', updateStayEstimate);
        checkInInput.addEventListener('change', () => {
            if (checkInInput.value) {
                const next = new Date(checkInInput.value + 'T00:00:00');
                next.setDate(next.getDate() + 1);
                checkOutInput.min = next.toISOString().split('T')[0];
            }
            updateStayEstimate();
        });
        checkOutInput.addEventListener('change', updateStayEstimate);
    }

    // 3. Búsqueda Instantánea
    const searchInput = document.getElementById('htLiveSearch');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const q = e.target.value.toLowerCase().trim();
            document.querySelectorAll('[data-searchable]').forEach(item => {
                item.style.display = item.textContent.toLowerCase().includes(q) ? '' : 'none';
            });
        });
    }

    // 4. Filtro por Estado
    document.querySelectorAll('[data-filter-status]').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('[data-filter-status]').forEach(b => b.classList.remove('ht-btn-forest'));
            btn.classList.add('ht-btn-forest');
            const st = btn.dataset.filterStatus;
            document.querySelectorAll('[data-res-status]').forEach(row => {
                row.style.display = (st === 'ALL' || row.dataset.resStatus === st) ? '' : 'none';
            });
        });
    });

    // 5. Calendario Interactivo de Ocupación (Construido desde datos reales del DOM)
    const calContainer = document.getElementById('htOccupancyCalendar');
    const calMonthLabel = document.getElementById('htCalMonthLabel');
    const prevMonthBtn = document.getElementById('htCalPrev');
    const nextMonthBtn = document.getElementById('htCalNext');

    if (calContainer) {
        const roomsData = Array.from(document.querySelectorAll('#htRoomMeta span')).map(el => ({
            id: el.dataset.id,
            number: el.dataset.number,
            type: el.dataset.type
        }));

        const guestsMap = {};
        document.querySelectorAll('#htGuestMeta span').forEach(el => {
            guestsMap[el.dataset.id] = el.dataset.name;
        });

        const resData = Array.from(document.querySelectorAll('#htResMeta span')).map(el => ({
            id: el.dataset.id,
            roomId: el.dataset.roomId,
            guest: guestsMap[el.dataset.guestId] || ('Huésped #' + el.dataset.guestId),
            checkIn: el.dataset.checkIn,
            checkOut: el.dataset.checkOut,
            status: el.dataset.status
        })).filter(r => r.status !== 'CANCELLED');

        let viewDate = new Date();
        viewDate.setDate(1);

        function renderCalendar() {
            const year = viewDate.getFullYear();
            const month = viewDate.getMonth();
            const daysInMonth = new Date(year, month + 1, 0).getDate();
            const monthName = viewDate.toLocaleDateString('es-ES', { month: 'long', year: 'numeric' });
            if (calMonthLabel) calMonthLabel.textContent = monthName.charAt(0).toUpperCase() + monthName.slice(1);

            let html = '<table class="ht-cal-table"><thead><tr><th>Suite</th>';
            for (let d = 1; d <= daysInMonth; d++) {
                html += `<th>${String(d).padStart(2, '0')}</th>`;
            }
            html += '</tr></thead><tbody>';

            roomsData.forEach(room => {
                html += `<tr><td>Suite ${room.number} <span style="display:block;font-size:0.7rem;color:var(--ink-muted);font-weight:400;">${room.type}</span></td>`;
                for (let d = 1; d <= daysInMonth; d++) {
                    const iso = `${year}-${String(month + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
                    const match = resData.find(r => r.roomId === room.id && iso >= r.checkIn && iso < r.checkOut);
                    if (match) {
                        html += `<td title="${match.guest} (${match.checkIn} → ${match.checkOut})"><span class="ht-cal-dot ht-cal-${match.status}">${match.guest.split(' ')[0]}</span></td>`;
                    } else {
                        html += `<td></td>`;
                    }
                }
                html += '</tr>';
            });

            html += '</tbody></table>';
            calContainer.innerHTML = roomsData.length ? html : '<p style="color:var(--ink-muted);padding:1rem;">No hay habitaciones registradas para proyectar en el calendario.</p>';
        }

        renderCalendar();
        prevMonthBtn?.addEventListener('click', () => { viewDate.setMonth(viewDate.getMonth() - 1); renderCalendar(); });
        nextMonthBtn?.addEventListener('click', () => { viewDate.setMonth(viewDate.getMonth() + 1); renderCalendar(); });
    }

    // 6. Llaves Digitales de Login
    document.querySelectorAll('[data-fill-email]').forEach(chip => {
        chip.addEventListener('click', () => {
            const email = document.getElementById('loginEmail');
            const pass = document.getElementById('loginPassword');
            if (email && pass) {
                email.value = chip.dataset.fillEmail;
                pass.value = chip.dataset.fillPass;
            }
        });
    });
});