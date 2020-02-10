INSERT IGNORE INTO `role` (`role_id`, `role_name`) VALUES
	(3, 'ROLE_MENTOR'),
	(2, 'ROLE_HR'),
	(1, 'ROLE_ADMIN');


INSERT IGNORE INTO user (user_id, user_name, user_email, user_password) VALUES
	(1,'Admin', 'admin@aroha.co.in', '$2a$10$7sFPuv1oAYlgVQSzzCdQwe5fo28SYUJZ7jsIdJJXtMfccghn7sknq');

INSERT IGNORE INTO `user_roles` (`user_Id`, `role_Id`) VALUES
	(1, 1);