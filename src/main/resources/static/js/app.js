// DecisionSim — Global JS

document.addEventListener('DOMContentLoaded', () => {

  // ── Auto-dismiss alerts ──────────────────────────────────────
  document.querySelectorAll('.alert').forEach(el => {
    const close = el.querySelector('.alert-close');
    if (close) close.addEventListener('click', () => {
      el.style.opacity = '0'; el.style.transform = 'translateY(-8px)';
      el.style.transition = 'all .2s ease';
      setTimeout(() => el.remove(), 200);
    });
    setTimeout(() => {
      el.style.opacity = '0'; el.style.transform = 'translateY(-8px)';
      el.style.transition = 'all .4s ease';
      setTimeout(() => el.remove(), 400);
    }, 5000);
  });

  // ── Modal open/close ─────────────────────────────────────────
  document.querySelectorAll('[data-modal]').forEach(btn => {
    btn.addEventListener('click', () => {
      const id = btn.dataset.modal;
      const modal = document.getElementById(id);
      if (modal) modal.classList.add('active');
    });
  });
  document.querySelectorAll('.modal-overlay').forEach(overlay => {
    overlay.addEventListener('click', e => {
      if (e.target === overlay) overlay.classList.remove('active');
    });
    overlay.querySelectorAll('.modal-close, [data-modal-close]').forEach(btn => {
      btn.addEventListener('click', () => overlay.classList.remove('active'));
    });
  });
  document.addEventListener('keydown', e => {
    if (e.key === 'Escape') document.querySelectorAll('.modal-overlay.active').forEach(m => m.classList.remove('active'));
  });

  // ── Collapse toggles ─────────────────────────────────────────
  document.querySelectorAll('[data-collapse]').forEach(btn => {
    btn.addEventListener('click', () => {
      const target = document.getElementById(btn.dataset.collapse);
      if (!target) return;
      target.classList.toggle('show');
      const icon = btn.querySelector('.collapse-icon');
      if (icon) icon.style.transform = target.classList.contains('show') ? 'rotate(180deg)' : '';
    });
  });

  // ── Animated counters ────────────────────────────────────────
  document.querySelectorAll('[data-count]').forEach(el => {
    const target = parseInt(el.dataset.count, 10);
    const duration = 800;
    const start = performance.now();
    const update = (time) => {
      const progress = Math.min((time - start) / duration, 1);
      const eased = 1 - Math.pow(1 - progress, 3);
      el.textContent = Math.floor(eased * target);
      if (progress < 1) requestAnimationFrame(update);
      else el.textContent = target;
    };
    requestAnimationFrame(update);
  });

  // ── Progress bars: animate width on load ─────────────────────
  document.querySelectorAll('.progress-fill[data-width]').forEach(bar => {
    bar.style.width = '0%';
    requestAnimationFrame(() => {
      requestAnimationFrame(() => { bar.style.width = bar.dataset.width + '%'; });
    });
  });

  // ── Active nav link highlight ────────────────────────────────
  const path = window.location.pathname;
  document.querySelectorAll('.nav-links a').forEach(a => {
    const href = a.getAttribute('href');
    if (href && path.startsWith(href) && href !== '/') a.classList.add('active');
    if (href === '/' && path === '/') a.classList.add('active');
  });

  // ── Table row clickable ───────────────────────────────────────
  document.querySelectorAll('tr[data-href]').forEach(row => {
    row.style.cursor = 'pointer';
    row.addEventListener('click', e => {
      if (!e.target.closest('button, a, form')) window.location.href = row.dataset.href;
    });
  });
});
