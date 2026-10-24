import UIKit

/// 断网 / 加载失败时显示的全屏提示面板。
final class ErrorView: UIView {

    var onRetry: (() -> Void)?

    private let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "加载失败"
        label.textColor = .white
        label.font = .boldSystemFont(ofSize: 20)
        label.textAlignment = .center
        return label
    }()

    private let messageLabel: UILabel = {
        let label = UILabel()
        label.text = "网络好像不太稳，检查一下连接再重试吧"
        label.textColor = UIColor(red: 184.0 / 255.0, green: 179.0 / 255.0, blue: 208.0 / 255.0, alpha: 1)
        label.font = .systemFont(ofSize: 14)
        label.textAlignment = .center
        label.numberOfLines = 0
        return label
    }()

    private let iconView: UIImageView = {
        let imageView = UIImageView(image: UIImage(named: "SplashIcon"))
        imageView.contentMode = .scaleAspectFit
        return imageView
    }()

    private lazy var retryButton: UIButton = {
        let button = UIButton(type: .system)
        var configuration = UIButton.Configuration.filled()
        configuration.contentInsets = NSDirectionalEdgeInsets(top: 12, leading: 28, bottom: 12, trailing: 28)
        button.configuration = configuration
        button.setTitle("重新加载", for: .normal)
        button.setTitleColor(.white, for: .normal)
        button.addTarget(self, action: #selector(retryTapped), for: .touchUpInside)
        return button
    }()

    override init(frame: CGRect) {
        super.init(frame: frame)
        backgroundColor = UIColor(red: 26.0 / 255.0, green: 22.0 / 255.0, blue: 48.0 / 255.0, alpha: 1)

        let stack = UIStackView(arrangedSubviews: [iconView, titleLabel, messageLabel, retryButton])
        stack.axis = .vertical
        stack.alignment = .center
        stack.spacing = 16
        stack.translatesAutoresizingMaskIntoConstraints = false
        addSubview(stack)

        NSLayoutConstraint.activate([
            iconView.heightAnchor.constraint(equalToConstant: 96),
            iconView.widthAnchor.constraint(equalToConstant: 96),
            stack.centerYAnchor.constraint(equalTo: centerYAnchor),
            stack.centerXAnchor.constraint(equalTo: centerXAnchor),
            stack.leadingAnchor.constraint(greaterThanOrEqualTo: leadingAnchor, constant: 32),
            stack.trailingAnchor.constraint(lessThanOrEqualTo: trailingAnchor, constant: -32)
        ])
    }

    @available(*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    @objc private func retryTapped() {
        onRetry?()
    }
}
