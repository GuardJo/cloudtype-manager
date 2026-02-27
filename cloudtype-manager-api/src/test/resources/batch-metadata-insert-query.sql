-- job_instance --
INSERT INTO public.batch_job_instance (job_instance_id, version, job_name, job_key)
VALUES (1, 0, 'updateAllServerStatusJob', '1478fef41a7513c3cb9d4949e530f7db');
INSERT INTO public.batch_job_instance (job_instance_id, version, job_name, job_key)
VALUES (2, 0, 'updateAllServerStatusJob', 'd36c3e251f75649bebcce0605c47ed86');

-- job_execution --
INSERT INTO public.batch_job_execution (job_execution_id, version, job_instance_id, create_time, start_time, end_time,
                                        status, exit_code, exit_message, last_updated)
VALUES (1, 2, 1, '2025-02-27 16:33:32.392925', '2025-02-27 16:33:32.431382', '2025-02-27 16:33:32.512795', 'COMPLETED',
        'COMPLETED', '', '2025-02-27 16:33:32.512858');
INSERT INTO public.batch_job_execution (job_execution_id, version, job_instance_id, create_time, start_time, end_time,
                                        status, exit_code, exit_message, last_updated)
VALUES (2, 2, 2, '2025-02-27 16:34:56.708281', '2025-02-27 16:34:56.723178', '2025-02-27 16:34:56.798261', 'FAILED',
        'COMPLETED', '', '2025-02-27 16:34:56.798322');

-- job_execution_context
INSERT INTO public.batch_job_execution_context (job_execution_id, short_context, serialized_context)
VALUES (1,
        'rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMi4yeA==',
        null);
INSERT INTO public.batch_job_execution_context (job_execution_id, short_context, serialized_context)
VALUES (2,
        'rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMi4yeA==',
        null);

-- job_execution_params
INSERT INTO public.batch_job_execution_params (job_execution_id, parameter_name, parameter_type, parameter_value,
                                               identifying)
VALUES (1, 'time', 'java.lang.Long', '1772177612361', 'Y');
INSERT INTO public.batch_job_execution_params (job_execution_id, parameter_name, parameter_type, parameter_value,
                                               identifying)
VALUES (2, 'time', 'java.lang.Long', '1772177696679', 'Y');

-- step_execution
INSERT INTO public.batch_step_execution (step_execution_id, version, step_name, job_execution_id, create_time,
                                         start_time, end_time, status, commit_count, read_count, filter_count,
                                         write_count, read_skip_count, write_skip_count, process_skip_count,
                                         rollback_count, exit_code, exit_message, last_updated)
VALUES (1, 3, 'updateAllServerStatusStep', 1, '2025-02-27 16:33:32.441375', '2025-02-27 16:33:32.444917',
        '2025-02-27 16:33:32.507415', 'COMPLETED', 1, 0, 0, 0, 0, 0, 0, 0, 'COMPLETED', '',
        '2025-02-27 16:33:32.509101');
INSERT INTO public.batch_step_execution (step_execution_id, version, step_name, job_execution_id, create_time,
                                         start_time, end_time, status, commit_count, read_count, filter_count,
                                         write_count, read_skip_count, write_skip_count, process_skip_count,
                                         rollback_count, exit_code, exit_message, last_updated)
VALUES (2, 3, 'updateAllServerStatusStep', 2, '2025-02-27 16:34:56.733687', '2025-02-27 16:34:56.737437',
        '2025-02-27 16:34:56.792979', 'COMPLETED', 1, 0, 0, 0, 0, 0, 0, 0, 'COMPLETED', '',
        '2025-02-27 16:34:56.794744');

-- step_execution_context
INSERT INTO public.batch_step_execution_context (step_execution_id, short_context, serialized_context)
VALUES (1,
        'rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAAEdAARYmF0Y2gudGFza2xldFR5cGV0AD1vcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC5pdGVtLkNodW5rT3JpZW50ZWRUYXNrbGV0dAAfc2VydmVySW5mb0l0ZW1SZWFkZXIucmVhZC5jb3VudHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMi4ydAAOYmF0Y2guc3RlcFR5cGV0ADdvcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC50YXNrbGV0LlRhc2tsZXRTdGVweA==',
        null);
INSERT INTO public.batch_step_execution_context (step_execution_id, short_context, serialized_context)
VALUES (2,
        'rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAAEdAARYmF0Y2gudGFza2xldFR5cGV0AD1vcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC5pdGVtLkNodW5rT3JpZW50ZWRUYXNrbGV0dAAfc2VydmVySW5mb0l0ZW1SZWFkZXIucmVhZC5jb3VudHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMi4ydAAOYmF0Y2guc3RlcFR5cGV0ADdvcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC50YXNrbGV0LlRhc2tsZXRTdGVweA==',
        null);

